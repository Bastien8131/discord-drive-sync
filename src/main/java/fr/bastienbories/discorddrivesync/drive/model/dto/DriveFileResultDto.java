package fr.bastienbories.discorddrivesync.drive.model.dto;

import java.util.List;

public record DriveFileResultDto(List<SucceededItemDto> succeededItems, List<FailureItemDto> failureItems) {
    public record SucceededItemDto(String filename) {}
    public record FailureItemDto(String filename, String reason) {}
}