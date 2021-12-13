package com.ninetailsoftware.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import com.ninetailsoftware.commands.CommandRouter;
import com.ninetailsoftware.commands.Help;
import com.ninetailsoftware.commands.Initiative;
import com.ninetailsoftware.commands.Roll;
import com.ninetailsoftware.commands.Status;

public class DiceRoller {

	private static CommandRouter router= new CommandRouter();

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
			if (event.getMessageContent().startsWith("!")) {
				router.routeCommand(event.getMessageContent(), event.getMessageAuthor().getDisplayName(),
						event.getChannel());
			}
		});

		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().startsWith("!quit")
					&& event.getMessageAuthor().getDiscriminatedName().equalsIgnoreCase("tierran#9477")) {
				System.exit(0);
			}
		});
	}
}
