package fr.bastienbories.discorddrivesync.discord.services;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DiscordBotServices extends ListenerAdapter {
    private final JDA jda;

    public DiscordBotServices() throws InterruptedException {
        Dotenv dotenv = Dotenv.load();
        this.jda = JDABuilder.createDefault(dotenv.get("DEV_BOT_TOKEN"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(this)
                .setActivity(Activity.watching("your messages"))
                .build();
        this.jda.awaitReady();
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
    }

    @Override
    public void onChannelDelete(@NonNull ChannelDeleteEvent event) {
        super.onChannelDelete(event);
    }



    //Methode & Function

    public List<Member> getMembers(){
        Guild guild = jda.getGuilds().getFirst();
        return guild.loadMembers().get();
    }
}
