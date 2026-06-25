package fr.bastienbories.discorddrivesync.discord.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean(name = "discordTaskExecutor")
    public Executor discordTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
