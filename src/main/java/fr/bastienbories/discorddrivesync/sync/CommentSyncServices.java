package fr.bastienbories.discorddrivesync.sync;

import fr.bastienbories.discorddrivesync.common.LogMessages;
import fr.bastienbories.discorddrivesync.core.model.CoreContent;
import fr.bastienbories.discorddrivesync.core.services.CoreContentServices;
import fr.bastienbories.discorddrivesync.discord.model.*;
import fr.bastienbories.discorddrivesync.discord.services.*;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class CommentSyncServices {

    private static final Logger log = LoggerFactory.getLogger(CommentSyncServices.class);

    private final DiscordApiServices discordApiServices;

    private final DiscordUserServices discordUserServices;
    private final DiscordChannelServices discordChannelServices;
    private final CoreContentServices coreContentServices;
    private final DiscordMessageServices discordMessageServices;
    private final DiscordCommentServices discordCommentServices;

    public CommentSyncServices(DiscordApiServices discordApiServices, DiscordUserServices discordUserServices, DiscordChannelServices discordChannelServices, CoreContentServices coreContentServices, DiscordMessageServices discordMessageServices, DiscordCommentServices discordCommentServices) {
        this.discordApiServices = discordApiServices;
        this.discordUserServices = discordUserServices;
        this.discordChannelServices = discordChannelServices;
        this.coreContentServices = coreContentServices;
        this.discordMessageServices = discordMessageServices;
        this.discordCommentServices = discordCommentServices;
    }

    public void commentMessageFormDiscord(Message comment) {
        discordApiServices.deleteMessage(comment);
        if (comment.getReferencedMessage() == null) return;

        long authorId = comment.getAuthor().getIdLong();
        long refMessageId = comment.getReferencedMessage().getIdLong();

        discordUserServices.getOrFetchById(authorId).ifPresentOrElse(
                discordUser -> {
                    CoreContent commentContent = new CoreContent(comment.getContentRaw(), discordUser);
                    coreContentServices.save(commentContent);
                    discordMessageServices.getById(refMessageId).ifPresentOrElse(
                            discordRefMessage -> {
                                CoreContent refContent = discordRefMessage.getContent();
                                List<DiscordMessage> discordMessageTargetsList = discordMessageServices.getListByData(refContent);
                                discordApiServices.sendMultipleComment(discordMessageTargetsList, comment).thenAccept(botComments -> {
                                    for (Message botComment: botComments){
                                        long botChannelId = botComment.getChannelIdLong();
                                        Message botRefMessage = Objects.requireNonNull(botComment.getReferencedMessage());
                                        long botRefMessageId = botRefMessage.getIdLong();
                                        discordChannelServices.getById(botChannelId).ifPresentOrElse(
                                                discordChannel -> discordMessageTargetsList.stream().filter(discordMessage -> discordMessage.getId() == botRefMessageId).findFirst().ifPresentOrElse(
                                                        discordMessage -> {
                                                            DiscordComment discordComment = new DiscordComment(
                                                                    botComment.getIdLong(),
                                                                    commentContent,
                                                                    discordUser,
                                                                    discordChannel,
                                                                    discordMessage,
                                                                    null
                                                            );
                                                            discordCommentServices.save(discordComment);
                                                        },
                                                        () -> LogMessages.notFoundInTheList(log, DiscordMessage.class, botRefMessageId, discordMessageTargetsList)
                                                ),
                                                () -> LogMessages.notFoundInDatabase(log, DiscordChannel.class, botChannelId)
                                        );
                                    }
                                }).exceptionally(
                                        ex -> {
                                            LogMessages.unexpectedErrorDuringAsyncProcessing(log, ex);
                                            return null;
                                        }
                                );
                            },
                            () -> LogMessages.notFoundInDatabase(log, DiscordMessage.class, refMessageId)
                    );
                },
                () -> LogMessages.notFoundInDatabase(log, DiscordUser.class, authorId)
        );
    }
}
