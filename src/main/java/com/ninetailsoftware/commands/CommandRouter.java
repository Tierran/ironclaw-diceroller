package com.ninetailsoftware.commands;

import org.javacord.api.entity.channel.TextChannel;

public class CommandRouter {
	private static Roll roller = new Roll();
	private static Initiative init = new Initiative();
	private static Status status = new Status();
	private static Help help = new Help();

	public void routeCommand(String messageContent, String displayName, TextChannel channel) {
		messageContent = messageContent.toLowerCase();
		String[] commandArray = messageContent.split(" ");

		String command = commandArray[0];

		switch (command) {
		case "!roll":
		case "!r":
		case "!counter":
		case "!parry":
		case "!dodge":
		case "!flip":
			roller.rollDice(messageContent, displayName, channel);
			break;
		case "!init":
			init.initativeRoll(messageContent, displayName, channel);
			break;
		case "!status":
		case "!s":
			status.statusCommand(messageContent, channel);
			break;
		case "!help":
			help.showHelp(channel);
			break;
		}

	}

}
