package fr.bastienbories.discorddrivesync.core.model.dto;

public record CoreUploadLinkSummaryDto(
    long id,
    String token,
    boolean used,
    long userId,
    long channelId
) {}
