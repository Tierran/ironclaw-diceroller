package com.ninetailsoftware.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;

import com.ninetailsoftware.diceroller.model.Character;

public class Status {

	private static HashMap<String, Character> characters = new HashMap<String, Character>();

	public void statusCommand(String messageContent, TextChannel channel) {
		// Format should be !status <command> <who> <status>, <status>, ...

		messageContent = messageContent.toLowerCase();

		String command = messageContent.split(" ")[1];

		switch (command) {
		case "add":
			addStatus(messageContent);
			break;
		case "remove":
			removeStatus(messageContent);
			break;
		case "all":
			showAllStatus(channel);
			break;
		case "reset":
			resetStatus();
			break;
		case "drop":
			dropCharacter(messageContent);
			break;
		}
	}

	private void dropCharacter(String messageContent) {
		String characterName = this.capitalizeAll(messageContent.split(" ")[2]);
		characters.remove(characterName);
	}

	private void addStatus(String messageContent) {
		String characterName = messageContent.split(" ")[2];
		String characterType = messageContent.split(" ")[3];
		messageContent = messageContent.substring(messageContent.indexOf(" ", messageContent.indexOf(characterName)))
				.trim();

		characterName = this.capitalizeAll(characterName);

		if (characterType.toUpperCase().contains("PC")) {
			messageContent = messageContent.replace(characterType, "").trim();
		}

		ArrayList<String> statuses = new ArrayList<String>();
		if (messageContent.length() > 0) {
			for (int i = 0; i < messageContent.split(",").length; i++) {
				statuses.add(messageContent.split(",")[i].trim());
			}
		}

		Character character;
		if (characters.get(characterName) == null) {
			character = new Character();
			character.setName(characterName);
			characters.put(characterName, character);
		} else {
			character = characters.get(characterName);
		}
		if (characterType.toUpperCase().contains("PC")) {
			character.setType(characterType.toUpperCase());
		}

		for (int i = 0; i < statuses.size(); i++) {
			if (!statuses.get(i).equals("")) {
				character.getStatus().add(this.capitalizeAll(statuses.get(i)));
			}
		}
	}

	private void removeStatus(String messageContent) {
		String characterName = messageContent.split(" ")[2];
		messageContent = messageContent.substring(messageContent.indexOf(" ", messageContent.indexOf(characterName)))
				.trim();

		characterName = this.capitalizeAll(characterName);

		ArrayList<String> statuses = new ArrayList<String>();
		for (int i = 0; i < messageContent.split(",").length; i++) {
			statuses.add(messageContent.split(",")[i].trim());
		}

		Character character;
		if (characters.get(characterName) != null) {
			character = characters.get(characterName);
			for (int i = 0; i < statuses.size(); i++) {
				character.getStatus().remove(this.capitalizeAll(statuses.get(i)));
			}
		}
	}

	private void showAllStatus(TextChannel channel) {
		MessageBuilder mb = new MessageBuilder();
		ArrayList<String> keys = new ArrayList<String>(characters.keySet());

		Boolean pc = false;
		Boolean npc = false;
		Boolean unknown = false;

		for (int i = 0; i < keys.size(); i++) {
			Character character = characters.get(keys.get(i));
			if (character.getType() != null && character.getType().equals("PC")) {
				if (!pc) {
					mb.append("PCs", MessageDecoration.BOLD).appendNewLine();
					pc = true;
				}
				mb.append(character.getName() + " ").append(character.getStatus().toString()).appendNewLine();
			}
		}

		for (int i = 0; i < keys.size(); i++) {
			Character character = characters.get(keys.get(i));
			if (character.getType() != null && character.getType().equals("NPC")) {
				if (!npc) {
					mb.append("NPCs", MessageDecoration.BOLD).appendNewLine();
					npc = true;
				}

				mb.append(character.getName() + " ").append(character.getStatus().toString()).appendNewLine();
			}
		}

		for (int i = 0; i < keys.size(); i++) {
			Character character = characters.get(keys.get(i));
			if (character.getType() == null) {
				if (!unknown) {
					mb.append("NPCs", MessageDecoration.BOLD).appendNewLine();
					unknown = true;
				}

				mb.append(character.getName() + " ").append(character.getStatus().toString()).appendNewLine();
			}
		}
		mb.send(channel);
	}

	private String capitalizeAll(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		return Arrays.stream(str.split("\\s+")).map(t -> t.substring(0, 1).toUpperCase() + t.substring(1))
				.collect(Collectors.joining(" "));
	}

	public void addStatus(String userName, String type, String status) {
		// TODO Auto-generated method stub
		Character character;
		userName = this.capitalizeAll(userName.toLowerCase());
		if (characters.get(userName) == null) {
			character = new Character();
			character.setName(userName);
			character.setType(type);
			characters.put(userName, character);
		} else {
			character = characters.get(userName);
		}

		if (status != null) {
			character.getStatus().add(status);
		}
	}

	private void resetStatus() {
		characters.clear();
	}
}
