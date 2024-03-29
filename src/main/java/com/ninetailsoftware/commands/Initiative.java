package com.ninetailsoftware.commands;

import java.util.ArrayList;
import java.util.LinkedList;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class Initiative {

	Roll roll = new Roll();

	private static LinkedList<InitRoll> turnOrder = new LinkedList<InitRoll>();
	private static String tiebreaker = "PC";

	public void initativeRoll(String messageContent, String userName, TextChannel channel) {
		EmbedBuilder _retVal = new EmbedBuilder();

		ArrayList<Integer> initRolls = new ArrayList<Integer>();
		Integer successes = 0;
		InitRoll ir = new InitRoll();
		Boolean tied = false;

		String characterName = userName;

		messageContent = messageContent.toLowerCase();

		if (messageContent.equalsIgnoreCase("!init")) {
			channel.sendMessage(this.buildTurnOrder());
			return;
		}

		if (messageContent.contains("!init reset")) {
			this.resetInitiative();
			return;
		}

		if (messageContent.contains("!init set")) {
			this.setInitiative(messageContent);
			channel.sendMessage(this.buildTurnOrder());
			return;
		}
		
		if (messageContent.contains("!init drop")) {
			this.dropLastFromInitiative(messageContent.split(" ")[2]);
			channel.sendMessage(this.buildTurnOrder());
			return;
		}

		if (messageContent.contains("npc") || messageContent.contains("pc")) {

			if (messageContent.contains("npc")) {
				ir.setType("NPC");

				if (messageContent.split(" ").length == 4)
					characterName = messageContent.split(" ")[3];

				messageContent = messageContent.replace("npc", "");
				messageContent = messageContent.replace(characterName, "");
			} else {
				ir.setType("PC");
				
				if (messageContent.split(" ").length == 4)
					characterName = messageContent.split(" ")[3];
				
				messageContent = messageContent.replace("pc", "");
				messageContent = messageContent.replace(characterName, "");
			}

			messageContent = messageContent.substring(5).trim();

			initRolls = roll.roll(messageContent.substring(0, messageContent.toLowerCase().indexOf('t')));
			for (int i = 0; i < initRolls.size(); i++) {
				ir.setRoll(ir.getRoll() + initRolls.get(i));
			}
			Integer target = Integer.parseInt(messageContent.substring(messageContent.toLowerCase().indexOf('t') + 1));
			ir.setSuccesses(roll.compareRolls(initRolls, target));
			if (ir.getSuccesses() == 0 && roll.compareRolls(initRolls, target - 1) >= 1) {
				tied = true;
			}

			addNewInit(ir);
		} else {
			new MessageBuilder().append(userName, MessageDecoration.BOLD).append(" Init Roll Should Include NPC or PC")
					.send(channel);

			return;
		}

		String rolledText = " Total: " + ir.getRoll().toString();

		if (tied) {
			rolledText += " (TIED)";
		}

		setStatusOnInit(ir, characterName, initRolls);

		MessageBuilder mb = new MessageBuilder();
		mb.append(userName, MessageDecoration.BOLD)
				.append(" rolled: " + roll.buildResultString(ir.getSuccesses(), initRolls)).appendNewLine()
				.append(rolledText).appendNewLine();

		if (ir.getSuccesses() == 0) {
			Boolean botch = true;
			for (int i = 0; i < initRolls.size(); i++) {
				if (initRolls.get(i) > 1) {
					botch = false;
					break;
				}
			}
			if (botch) {
				mb.append("You enter the battle Reeling!").appendNewLine();
			} else if (tied) {
				mb.append("Willing! Take a Ready action but then you will be Reeling!").appendNewLine();
			} else {
				mb.append("Able! Prepare to fight!").appendNewLine();
			}
		} else if (ir.getSuccesses() == 1) {
			mb.append("Ready! Take a Ready action and a Reload or Stride!").appendNewLine();
		} else if  (ir.getSuccesses() > 1) {
			mb.append("Focused! Take a Ready action and a Reload or Stride!").appendNewLine();
		}

		mb.appendNewLine().append(this.buildTurnOrder()).send(channel);
	}

	private void dropLastFromInitiative(String dropToken) {
		InitRoll ir;
		for(int i = turnOrder.size()-1; i>=0; i--) {
			ir = turnOrder.get(i);
			if(ir.getType().equalsIgnoreCase(dropToken)) {
				turnOrder.remove(ir);
				break;
			}
		}		
	}

	private void setStatusOnInit(InitRoll ir, String userName, ArrayList<Integer> initRolls) {

		String status = null;

		if (ir.getSuccesses() >= 2) {
			status = "Focused";
		}

		Boolean botch = true;
		for (int i = 0; i < initRolls.size(); i++) {
			if (initRolls.get(i) > 1) {
				botch = false;
				break;
			}
		}

		if (botch) {
			status = "Reeling";
		}

		Status s = new Status();
		s.addStatus(userName, ir.getType(), status);

	}

	private void addNewInit(InitRoll ir) {

		if (turnOrder.size() == 0) {
			turnOrder.add(ir);
			return;
		} else {
			for (int i = 0; i < turnOrder.size(); i++) {
				if (ir.getRoll() > turnOrder.get(i).getRoll()) {
					turnOrder.add(i, ir);
					return;
				} else if (ir.getRoll() == turnOrder.get(i).getRoll()) {
					if (turnOrder.get(i).getType().equalsIgnoreCase(ir.getType())) {
						turnOrder.add(i, ir);
						return;
					} else if (ir.getType().equalsIgnoreCase(tiebreaker)) {
						turnOrder.add(i, ir);
						if (tiebreaker.equalsIgnoreCase("NPC")) {
							tiebreaker = "PC";
						} else {
							tiebreaker = "NPC";
						}
						return;
					} else {
						if (tiebreaker.equalsIgnoreCase("NPC")) {
							tiebreaker = "PC";
						} else {
							tiebreaker = "NPC";
						}
					}
				}
			}
		}

		turnOrder.add(ir);
	}

	private void resetInitiative() {
		turnOrder = new LinkedList<InitRoll>();
	}

	private void setInitiative(String messageContent) {
		this.resetInitiative();

		messageContent = messageContent.replace("!init set", "").trim();
		for (int i = 0; i < messageContent.length(); i++) {
			InitRoll init = new InitRoll();
			turnOrder.add(init);
			if ((String.valueOf(messageContent.charAt(i)).equalsIgnoreCase("N")))
				init.setType("NPC");
			else if ((String.valueOf(messageContent.charAt(i)).equalsIgnoreCase("P")))
				init.setType("PC");
		}
	}

	public String buildTurnOrder() {
		String _retVal = new String();

		if (turnOrder.size() == 0) {
			return "No Initiative Set";
		}

		for (int i = 0; i < turnOrder.size(); i++) {

			if (turnOrder.get(i).getType().equalsIgnoreCase("npc")) {
				_retVal += ":imp:";
			} else {
				_retVal += ":smiley_cat:";
			}
		}

		return _retVal;
	}

	private class InitRoll {
		private String type;
		private Integer roll;
		private Integer successes;

		public InitRoll() {
			roll = 0;
			successes = 0;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Integer getRoll() {
			return roll;
		}

		public void setRoll(Integer roll) {
			this.roll = roll;
		}

		public Integer getSuccesses() {
			return successes;
		}

		public void setSuccesses(Integer successes) {
			this.successes = successes;
		}
	}
}
