package com.ninetailsoftware.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;

import com.ninetailsoftware.diceroller.model.Character;

public class Status {

	private static HashMap<String, Character> characters = new HashMap<String, Character>();

	public void setStatus(String messageContent) {
		// Format should be !status <command> <who> <status> <status> ...
		String characterName = messageContent.split(" ")[2];
		String command = messageContent.split(" ")[1];
		ArrayList<String> statuses = new ArrayList<String>();
		for (int i = 3; i < messageContent.split(" ").length; i++) {
			statuses.add(messageContent.split(" ")[i]);
		}

		Character character;
		if (characters.get(characterName) == null) {
			character = new Character();
			character.setName(characterName);
			characters.put(characterName, character);
		} else {
			character = characters.get(characterName);
		}
		for (int i = 0; i < statuses.size(); i++) {
			character.getStatus().add(statuses.get(i));
		}
	}

	public void showAllStatus(TextChannel channel) {
		MessageBuilder mb = new MessageBuilder();
		ArrayList<String> keys = new ArrayList<String>(characters.keySet());
		for (int i = 0; i < keys.size(); i++) {
			Character character = characters.get(keys.get(i));
			mb.append(character.getName() + " ").append(character.getStatus().toString()).appendNewLine();
		}
		mb.send(channel);
	}

	private void addStatus(String messageContent) {
		characters.get(messageContent).getStatus().add(messageContent);
	}

	private void removeStatus(String messageContent) {
		characters.get(messageContent).getStatus().remove(messageContent);
	}
}
