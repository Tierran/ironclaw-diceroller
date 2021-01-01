package com.ninetailsoftware.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;

public class Help {

	public void showHelp(TextChannel channel) {
		new MessageBuilder()
			.append("Commands : ", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("!roll").appendNewLine()
			.append("!init").appendNewLine().appendNewLine()
			.append("!roll Help:", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("!roll <favored><dice>t<target number> ").append("VS Target Number", MessageDecoration.BOLD).appendNewLine()
			.append("!roll <defense-type> <favored><dice>v<favored><dice> ").append("Opposed Role", MessageDecoration.BOLD).appendNewLine()
			.append("Options Key: ", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("Defense type (optional): COUNTER or PARRY").appendNewLine()
			.append("Favored use (optional): F").appendNewLine()
			.append("Dice Key: ", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("D12: 2").appendNewLine()
			.append("D10: 0").appendNewLine()
			.append("D8:   8").appendNewLine()
			.append("D6:   6").appendNewLine()
			.append("D4:   4").appendNewLine().appendNewLine()
			.append("!init Help:", MessageDecoration.BOLD, MessageDecoration.UNDERLINE).appendNewLine()
			.append("!init ").append("Displays Current Initiative Order", MessageDecoration.BOLD).appendNewLine()
			.append("!init <option><dice>t<target number> <NPC or PC>").appendNewLine()
			.append("!init reset ").append("Resets Initiative", MessageDecoration.BOLD).appendNewLine()
			.append("!init set <Ns or Ps>").append("Manually set Initiative Order (Use N for NPC & P for PC)", MessageDecoration.BOLD).appendNewLine()
			.send(channel);
	}
}
