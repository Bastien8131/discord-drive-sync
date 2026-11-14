package fr.bastienbories.discorddrivesync.discord.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

@Configuration
public class JdaConfig {

    @Value("${discord.bot.token}")
    private String botToken;

    private JDA jda;

    @Bean
    public JDA jda() throws InterruptedException {
        build();
        setupCommands();
        return jda;
    }

    private void build() throws InterruptedException {
        jda = JDABuilder.createDefault(botToken)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build()
                .awaitReady();
    }

    private void setupCommands() {
        List<SlashCommandData> commands = new ArrayList<>();
        DefaultMemberPermissions permission = DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL, Permission.MODERATE_MEMBERS);

        commands.add(Commands.slash("upload", "Uploading files that exceed Discord's limits via a web page."));

        for (SlashCommandData slashCommandData : commands) {
            slashCommandData.setDefaultPermissions(permission);
        }

        jda.updateCommands().addCommands(commands).queue();
    }
}