package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.core.model.CoreLabel;
import fr.bastienbories.discorddrivesync.core.services.CoreLabelServices;
import fr.bastienbories.discorddrivesync.discord.model.DiscordCategory;
import fr.bastienbories.discorddrivesync.discord.model.DiscordChannel;
import fr.bastienbories.discorddrivesync.discord.services.DiscordCategoryServices;
import fr.bastienbories.discorddrivesync.discord.services.DiscordChannelServices;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelSyncServicesTest {

    @Mock
    private DiscordCategoryServices discordCategoryServices;

    @Mock
    private DiscordChannelServices discordChannelServices;

    @Mock
    private CoreLabelServices coreLabelServices;

    @InjectMocks
    private ChannelSyncServices channelSyncServices;

    @Captor
    ArgumentCaptor<DiscordChannel> discordChannelArgumentCaptor;

    //name convention for test function: [méthodeTestée]_[scénario]_[résultatAttendu]
    @Test
    public void createChannelFromDiscord_discordCategoryExists_discordChannelCorrectlySaved(@Mock TextChannel channel){
        //Given
        long channelId = 1;
        String channelName = "Java";
        ChannelType channelType = ChannelType.TEXT;

        long categoryId = 10;
        String categoryName = "Dev";
        DiscordCategory discordCategory = new DiscordCategory(categoryId, categoryName);

        String labelName = channelName.toLowerCase();
        CoreLabel coreLabel = new CoreLabel(labelName);

        when(channel.getParentCategoryIdLong()).thenReturn(categoryId);
        when(channel.getName()).thenReturn(channelName);
        when(coreLabelServices.getOrCreateLabelByName(labelName)).thenReturn(coreLabel);
        when(discordCategoryServices.getById(categoryId)).thenReturn(Optional.of(discordCategory));
        when(channel.getIdLong()).thenReturn(channelId);
        when(channel.getType()).thenReturn(channelType);

        //When
        channelSyncServices.createChannelFromDiscord(channel);

        //Then
        verify(discordChannelServices).save(discordChannelArgumentCaptor.capture());
        DiscordChannel discordChannel = discordChannelArgumentCaptor.getValue();

        assertEquals(channelId, discordChannel.getId());
        assertEquals(channelName, discordChannel.getName());
        assertEquals(channelType, discordChannel.getType());
        assertEquals(coreLabel, discordChannel.getLabel());
        assertEquals(discordCategory, discordChannel.getDiscordCategory());
    }

    @Test
    public void createChannelFromDiscord_discordCategoryNotFound_discordChannelNeverSaved(@Mock TextChannel channel){
        //Given
        long categoryId = 20;

        when(channel.getParentCategoryIdLong()).thenReturn(categoryId);
        when(discordCategoryServices.getById(categoryId)).thenReturn(Optional.empty());

        //When
        channelSyncServices.createChannelFromDiscord(channel);

        //Then
        verify(discordChannelServices, never()).save(any());
        verify(coreLabelServices, never()).getOrCreateLabelByName(anyString());
    }
}
