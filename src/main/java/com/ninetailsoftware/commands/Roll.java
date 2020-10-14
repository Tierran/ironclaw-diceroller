package com.ninetailsoftware.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;

public class Roll {

	public void rollDice(String messageContent, String userName, TextChannel channel) {
		String resultString = new String();

		Boolean opposed = new Boolean(false);
		ArrayList<Integer> pcRolls = new ArrayList<Integer>();
		ArrayList<Integer> opposedRolls = new ArrayList<Integer>();
		Integer successes = null;

		messageContent = messageContent.substring(5).trim();

		if (messageContent.toLowerCase().contains("v")) {
			opposed = true;
			pcRolls = this.roll(messageContent.substring(0, messageContent.toLowerCase().indexOf('v')));
			opposedRolls = this.roll(messageContent.substring(messageContent.toLowerCase().indexOf('v') + 1));
			successes = this.compareRolls(pcRolls, opposedRolls);
		} else if (messageContent.toLowerCase().contains("t")) {
			pcRolls = this.roll(messageContent.substring(0, messageContent.toLowerCase().indexOf('t')));
			Integer target = Integer.parseInt(messageContent.substring(messageContent.toLowerCase().indexOf('t') + 1));
			successes = this.compareRolls(pcRolls, target);
		} else {
			pcRolls = this.roll(messageContent);
		}

		if (opposed) {
			resultString = buildResultString(successes, pcRolls, opposedRolls);
		} else {
			resultString = buildResultString(successes, pcRolls);
		}
		
		new MessageBuilder()
		.append(userName, MessageDecoration.BOLD)
		.append(" rolled: " + resultString).appendNewLine()
		.send(channel);
	}

	public String buildResultString(Integer successes, ArrayList<Integer>... rolls) {
		String _retVal = "";

		for (int i = 0; i < rolls[0].size(); i++) {
			if (i > 0) {
				_retVal += ", ";
			} else {
				_retVal += "[";
			}
			_retVal += rolls[0].get(i).toString();
		}

		_retVal += "]";

		if (rolls.length > 1) {
			_retVal += " V [";
			for (int i = 0; i < rolls[1].size(); i++) {
				if (i > 0) {
					_retVal += ", ";
				}
				_retVal += rolls[1].get(i).toString();
			}

			_retVal += "]";
		}

		if (successes != null) {
			_retVal += " Successes: " + successes.toString();
		}

		return _retVal;
	}

	public ArrayList<Integer> roll(String diceList) {
		ArrayList<Integer> rolls = new ArrayList<Integer>();
		ArrayList<Integer> dice = new ArrayList<Integer>();
		Random rand = new Random();
		Boolean favored = false;

		if (diceList.toLowerCase().contains("f")) {
			favored = true;
			diceList = diceList.replace("f", " ").trim();
		}

		diceList = diceList.replaceAll("\\s", "");

		for (int i = 0; i < diceList.length(); i++) {
			if (Integer.parseInt(String.valueOf(diceList.charAt(i))) >= 4)
				dice.add(Integer.parseInt(String.valueOf(diceList.charAt(i))));
			else
				dice.add(Integer.parseInt(String.valueOf(diceList.charAt(i))) + 10);
		}

		Collections.sort(dice, Collections.reverseOrder());

		for (int i = 0; i < dice.size(); i++) {

			if (dice.get(i).equals(12)) {
				rolls.add((rand.nextInt(12) + 1));

				if (favored && rolls.get(i).equals(1)) {
					System.out.println("D12 Rerolled");
					rolls.set(i, (rand.nextInt(12) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(10)) {
				rolls.add((rand.nextInt(10) + 1));

				if (favored && rolls.get(i).equals(1)) {
					System.out.println("D10 Rerolled");
					rolls.set(i, (rand.nextInt(10) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(8)) {
				rolls.add((rand.nextInt(8) + 1));

				if (favored && rolls.get(i).equals(1)) {
					System.out.println("D8 Rerolled");
					rolls.set(i, (rand.nextInt(8) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(6)) {
				rolls.add((rand.nextInt(6) + 1));

				if (favored && rolls.get(i).equals(1)) {
					System.out.println("D6 Rerolled");
					rolls.set(i, (rand.nextInt(6) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(4)) {
				rolls.add((rand.nextInt(4) + 1));
				if (favored && rolls.get(i).equals(1)) {
					System.out.println("D4 Rerolled");
					rolls.set(i, (rand.nextInt(4) + 1));
					favored = false;
				}
			}
		}

		return rolls;
	}

	public Integer compareRolls(ArrayList<Integer> pcRolls, ArrayList<Integer> opposedRolls) {
		Integer successes = 0;
		Integer maxRoll = Collections.max(opposedRolls);

		for (int i = 0; i < pcRolls.size(); i++) {
			if (pcRolls.get(i) > maxRoll) {
				successes++;
			}
		}

		return successes;
	}

	public Integer compareRolls(ArrayList<Integer> pcRolls, Integer target) {
		Integer successes = 0;

		for (int i = 0; i < pcRolls.size(); i++) {
			if (pcRolls.get(i) > target) {
				successes++;
			}
		}

		return successes;
	}
}
