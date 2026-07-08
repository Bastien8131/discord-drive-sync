package fr.bastienbories.discorddrivesync;

import fr.bastienbories.discorddrivesync.discord.services.DiscordApiServices;
import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = "discord.bot.token=dummy-token-for-tests")
class DiscordDriveSyncApplicationTests {

    @MockitoBean
    JDA jda;

    @MockitoBean
    DiscordApiServices discordApiServices;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.10-alpine");

    @Test
    void contextLoads() {
    }
}
