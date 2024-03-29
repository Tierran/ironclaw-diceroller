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

		String rollType = "roll";

		Boolean opposed = false;
		ArrayList<Integer> pcRolls = new ArrayList<Integer>();
		ArrayList<Integer> opposedRolls = new ArrayList<Integer>();
		Integer successes = null;

		// messageContent = messageContent.substring(5).trim();

		messageContent = messageContent.toLowerCase();
		/**
		 * Removing the roll command before proceeding to check other flags
		 */
		
		rollType = getRollType(messageContent);
		
		if(rollType.equalsIgnoreCase("flip")) {
			this.flip(userName, channel);
			return;
		}
		
		messageContent = messageContent.replace(messageContent.substring(0, messageContent.indexOf(" ")), "").trim();

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

		if (rollType.contentEquals("counter") && successes == 0) {
			Integer defenderSuccesses = this.compareRolls(opposedRolls, pcRolls);
			String defenderSuccesString = " wins with " + defenderSuccesses + " successes!";

			if (defenderSuccesses > 0) {

				if (defenderSuccesses == 1) {
					defenderSuccesString = " wins with " + defenderSuccesses + " success!";
				}
			} else if (defenderSuccesses == 0 && checkForTie(pcRolls, opposedRolls)) {
				defenderSuccesString = " and attacker are TIED";
				Integer target = Collections.max(opposedRolls) - 1;
				successes = compareRolls(pcRolls, target);
				defenderSuccesses = compareRolls(opposedRolls, target);
			}

			if (!checkForTie(pcRolls, opposedRolls)) {
				new MessageBuilder().append(userName, MessageDecoration.BOLD).append(" rolled: " + resultString)
						.appendNewLine().append("Defender", MessageDecoration.BOLD).append(defenderSuccesString)
						.send(channel);

				return;
			} else {

				String attackerSuccessMessage = "Attacker Successes: " + successes;
				String defenderSuccessMessage = "Defender Successes: " + defenderSuccesses;

				new MessageBuilder().append(userName, MessageDecoration.BOLD).append(" rolled: " + resultString)
						.appendNewLine().append("Defender", MessageDecoration.BOLD).append(defenderSuccesString)
						.appendNewLine().append(attackerSuccessMessage).appendNewLine().append(defenderSuccessMessage)
						.send(channel);
				return;
			}
		}

		if ((rollType.contentEquals("parry") || rollType.contentEquals("dodge"))
				&& checkForTie(pcRolls, opposedRolls)) {
			new MessageBuilder().append(userName, MessageDecoration.BOLD).append(" rolled: " + resultString)
					.appendNewLine().append("Defender", MessageDecoration.BOLD).append(" and attacker are TIED!")
					.appendNewLine().append("Retreat", MessageDecoration.BOLD).append(" or attack succeeds!")
					.send(channel);

			return;
		}

		new MessageBuilder().append(userName, MessageDecoration.BOLD).append(" rolled: " + resultString).appendNewLine()
				.send(channel);
	}

	private void flip(String userName, TextChannel channel) {
		Random rand = new Random();
		Integer flip = rand.nextInt(2);
		String earsOrTails;
		
		if(flip==0) {
			earsOrTails = "<:foxhead:879374850211336232>";
		}else {
			earsOrTails = "<:foxtail:879375194672734218>";
		}
		
		new MessageBuilder().append(userName, MessageDecoration.BOLD).append(" got: ").appendNewLine()
		.append(earsOrTails)
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
					rolls.set(i, (rand.nextInt(12) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(10)) {
				rolls.add((rand.nextInt(10) + 1));

				if (favored && rolls.get(i).equals(1)) {
					rolls.set(i, (rand.nextInt(10) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(8)) {
				rolls.add((rand.nextInt(8) + 1));

				if (favored && rolls.get(i).equals(1)) {
					rolls.set(i, (rand.nextInt(8) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(6)) {
				rolls.add((rand.nextInt(6) + 1));

				if (favored && rolls.get(i).equals(1)) {

					rolls.set(i, (rand.nextInt(6) + 1));
					favored = false;
				}
			}
			if (dice.get(i).equals(4)) {
				rolls.add((rand.nextInt(4) + 1));
				if (favored && rolls.get(i).equals(1)) {
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

	public Boolean checkForTie(ArrayList<Integer> challengeRoll, ArrayList<Integer> opposedRolls) {
		Boolean tied = false;
		Integer maxChallengeRoll = Collections.max(challengeRoll);
		Integer maxOpposedRoll = Collections.max(opposedRolls);

		if (maxChallengeRoll == maxOpposedRoll)
			tied = true;

		return tied;
	}

	public Boolean checkForTie(ArrayList<Integer> challengeRoll, Integer target) {
		Boolean tied = false;
		Integer maxChallengeRoll = Collections.max(challengeRoll);

		if (maxChallengeRoll == target)
			tied = true;

		return tied;
	}

	private String getRollType(String messageContent) {
		String _retVal = null;
		
		String command;
		if(messageContent.indexOf(" ")  != -1) {
			command = messageContent.substring(0, messageContent.indexOf(" "));
		} else {
			command = messageContent;
		}

		switch (command) {
			case "!roll":
			case "!r":
				_retVal = "roll";
				break;
			case "!counter":
				_retVal = "counter";
				break;
			case "!dodge":
				_retVal = "dodge";
				break;
			case "!parry":
				_retVal = "parry";
				break;
			case "!flip":
				_retVal = "flip";
				break;
		}

		return _retVal;
	}
}
