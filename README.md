# ironclaw-diceroller
Discord bot to support playing Ironclaw.

## Current Release:

Download the jar from the current release here:

https://github.com/Tierran/ironclaw-diceroller/releases/latest

## Getting Started

This bot uses the JavaCord library to interact with Discord.  To get started with a new bot, you will first need to set it up in your account.  Follow the directions here:

https://javacord.org/wiki/getting-started/creating-a-bot-account.html#create-a-bot-and-get-its-token

## Running the Bot

Make sure you have Java installed on the system you will be running this bot. This is only tested with OpenJDK8 : https://openjdk.java.net/install/

- Download the jar to the directory you would like to run this from
- Create a file in the same directory called "application.properties"
- This file needs a single line:
  - serverToken=\<token\> where \<token\> is the token you created during the bot creation process for Discord
Finally, this can be run using java -jar \<filename\> (example: java -jar ironclaw-diceroller-1.0.5.jar)
  
You should now have the bot in your server. If you would like the bot to permently run as a service, you can run the bot on a Raspberry Pi. 
