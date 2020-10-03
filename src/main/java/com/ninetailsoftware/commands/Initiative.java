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

		messageContent = messageContent.toLowerCase();
		
		if (messageContent.length() == 5) {
			channel.sendMessage(this.buildTurnOrder());
			return;
		}
		
		if(messageContent.contains("!init reset")) {
			this.resetInitiative();
			return;
		}
		
		if(messageContent.contains("!init set")) {
			this.setInitiative(messageContent);
			channel.sendMessage(this.buildTurnOrder());
			return;
		}
			
		if (messageContent.contains("npc") || messageContent.contains("pc")) {

			if (messageContent.contains("npc")) {
				ir.setType("NPC");
				messageContent = messageContent.replace("npc", " ");
			} else {
				ir.setType("PC");
				messageContent = messageContent.replace("pc", " ");
			}

			messageContent = messageContent.substring(5).trim();
			
			initRolls = roll.roll(messageContent.substring(0, messageContent.toLowerCase().indexOf('t')));
			for (int i = 0; i < initRolls.size(); i++) {
				ir.setRoll(ir.getRoll() + initRolls.get(i));
			}
			Integer target = Integer.parseInt(messageContent.substring(messageContent.toLowerCase().indexOf('t') + 1));
			ir.setSuccesses(roll.compareRolls(initRolls, target));

			addNewInit(ir);
		}

		new MessageBuilder()
			.append(userName, MessageDecoration.BOLD)
			.append(" Rolled: " + ir.getRoll().toString() + " Successes: "+ ir.getSuccesses().toString())
			.appendNewLine()
			.append(this.buildTurnOrder())
			.send(channel);
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
		for(int i = 0; i<messageContent.length(); i++) {
			InitRoll init = new InitRoll();
			turnOrder.add(init);
			if((String.valueOf(messageContent.charAt(i)).equalsIgnoreCase("N")))
					init.setType("NPC");
			else
				init.setType("PC");
		}
	}

	public String buildTurnOrder() {
		String _retVal = new String();

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
