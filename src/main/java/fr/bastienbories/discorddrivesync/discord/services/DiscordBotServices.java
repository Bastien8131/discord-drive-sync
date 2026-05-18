package fr.bastienbories.discorddrivesync.discord.services;

import fr.bastienbories.discorddrivesync.core.services.SyncServices;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
public class DiscordBotServices extends ListenerAdapter {

    private final SyncServices syncServices;
    private final JDA jda;

    public DiscordBotServices(SyncServices syncServices, JDA jda) {
        this.syncServices = syncServices;
        this.jda = jda;
    }

    @PostConstruct
    public void init() {
        jda.addEventListener(this);
    }

    //Listener

    @Override
    public void onReady(@NonNull ReadyEvent event) {
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
    }

    @Override
    public void onMessageDelete(@NonNull MessageDeleteEvent event) {
        super.onMessageDelete(event);
    }

    @Override
    public void onChannelCreate(@NonNull ChannelCreateEvent event) {
        super.onChannelCreate(event);
        switch (event.getChannelType()){
            case ChannelType.CATEGORY -> syncServices.createDiscordCategory((Category) event.getChannel());
            case ChannelType.TEXT -> System.out.println("yes");
            default -> System.out.println("no");
        }

    }

    @Override
    public void onChannelDelete(@NonNull ChannelDeleteEvent event) {
        super.onChannelDelete(event);
        switch (event.getChannelType()){
            case ChannelType.CATEGORY -> syncServices.deleteDiscordCategory((Category) event.getChannel());
            case ChannelType.TEXT -> System.out.println("yes");
            default -> System.out.println("no");
        }
    }



}
