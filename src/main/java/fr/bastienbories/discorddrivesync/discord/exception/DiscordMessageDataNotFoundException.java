package fr.bastienbories.discorddrivesync.discord.exception;

public class DiscordMessageDataNotFoundException extends RuntimeException {
    public DiscordMessageDataNotFoundException(String message) {
        super(message);
    }
}
