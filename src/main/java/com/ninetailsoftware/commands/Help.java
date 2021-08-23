package com.ninetailsoftware.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;

public class Help {

	public void showHelp(TextChannel channel) {
		new MessageBuilder()
			.append("Commands : ", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine().appendNewLine()
			.append("!roll Help:", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("!roll/!r <favored><dice>t<target number> ").append("VS Target Number", MessageDecoration.BOLD).appendNewLine()
			.append("!roll/!r <favored><dice>v<favored><dice> ").append("Opposed Roll", MessageDecoration.BOLD).appendNewLine()
			.append("!counter <favored><dice>v<favored><dice> ").append("Counter Attack Defense Roll", MessageDecoration.BOLD).appendNewLine()
			.append("!parry <favored><dice>v<favored><dice> ").append("Parry Defense Roll", MessageDecoration.BOLD).appendNewLine()
			.append("!dodge <favored><dice>v<favored><dice> ").append("Dodge Defense Roll", MessageDecoration.BOLD).appendNewLine()
			.append("!flip").append("Flip a coin!", MessageDecoration.BOLD).appendNewLine()
			.append("Options Key: ", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("Favored use (optional): F").appendNewLine()
			.append("Dice Key: ", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("D12: 2").appendNewLine()
			.append("D10: 0").appendNewLine()
			.append("D8:   8").appendNewLine()
			.append("D6:   6").appendNewLine()
			.append("D4:   4").appendNewLine().appendNewLine()
			.append("!init Help:", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("!init ").append("Displays Current Initiative Order", MessageDecoration.BOLD).appendNewLine()
			.append("!init <option><dice>t<target number> <NPC or PC> <Optional NPC Name> ").appendNewLine()
			.append("!init reset ").append("Resets Initiative", MessageDecoration.BOLD).appendNewLine()
			.append("!init set <N or P> ").append("Manually set Initiative Order (Use N for NPC & P for PC)", MessageDecoration.BOLD).appendNewLine().appendNewLine()
			.append("!status Help: ", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("!status add <name> <NPC or PC (optional)> <status, status, ...> ").append("Add status to character (will add character it is not currently on list)", MessageDecoration.BOLD).appendNewLine()
			.append("!status remove <name> <status, status, ...> ").append("Remove status from character", MessageDecoration.BOLD).appendNewLine()
			.append("!status drop ").append("Remove character from status list", MessageDecoration.BOLD).appendNewLine()
			.append("!status all ").append("Display status list", MessageDecoration.BOLD).appendNewLine()
			.append("!status reset ").append("Resets status list", MessageDecoration.BOLD).appendNewLine()
			.send(channel);
	}
}
