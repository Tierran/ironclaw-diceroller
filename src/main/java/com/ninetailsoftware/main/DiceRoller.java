package com.ninetailsoftware.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import com.ninetailsoftware.commands.Help;
import com.ninetailsoftware.commands.Initiative;
import com.ninetailsoftware.commands.Roll;

public class DiceRoller {

	private static Roll roller = new Roll();
	private static Initiative init = new Initiative();
	private static Help help = new Help();

	public void CreateInvite() {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream("./application.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String token = properties.getProperty("serverToken");

		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		System.out.println(api.createBotInvite());
	}

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("./application.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String token = properties.getProperty("serverToken");

		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().startsWith("!roll") || event.getMessageContent().startsWith("!r ")
					|| event.getMessageContent().startsWith("!counter")
					|| event.getMessageContent().startsWith("!parry")
					|| event.getMessageContent().startsWith("!dodge")) {
				roller.rollDice(event.getMessageContent(), event.getMessageAuthor().getDisplayName(),
						event.getChannel());
			}
		});

		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().startsWith("!init")) {
				init.initativeRoll(event.getMessageContent(), event.getMessageAuthor().getDisplayName(),
						event.getChannel());
			}
		});

		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().startsWith("!help")) {
				help.showHelp(event.getChannel());
			}
		});

		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().startsWith("!quit diceroller")
					&& event.getMessageAuthor().getDiscriminatedName().equalsIgnoreCase("tierran#9477")) {
				System.exit(0);
			}
		});
	}
}
