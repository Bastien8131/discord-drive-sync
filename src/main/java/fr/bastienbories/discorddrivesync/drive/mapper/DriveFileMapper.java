package fr.bastienbories.discorddrivesync.drive.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileDto;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileResultDto;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileResultDto.FailureItemDto;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileResultDto.SucceededItemDto;
import fr.bastienbories.discorddrivesync.drive.result.DriveFileResult;

@Mapper(componentModel = "spring")
public interface DriveFileMapper {
    DriveFileDto driveFileToDriveFileDto(DriveFile driveFile);

    @Mapping (target = "filename", source = "success.driveFile.name")
    SucceededItemDto successToSucceededItemDto(DriveFileResult.Success success);

    default FailureItemDto failureToFailureItemDto(DriveFileResult.Failure failure){
        String reason = switch (failure.throwable()) { //temporary; to be improved later by planning for multiple messages.
            default -> "An unexpected error occurred during the processing of this file";
        };

        return new FailureItemDto(failure.filename(), reason);
    }

    default DriveFileResultDto resultsToResultsDto(List<DriveFileResult> results) {
        List<SucceededItemDto> succeededItemDtos = new ArrayList<>();
        List<FailureItemDto> failureItemDtos = new ArrayList<>(); 

        for (DriveFileResult result : results) {
            switch (result) {
                case DriveFileResult.Success success -> succeededItemDtos.add(successToSucceededItemDto(success));
                case DriveFileResult.Failure failure -> failureItemDtos.add(failureToFailureItemDto(failure));
            }
        }
        
        return new DriveFileResultDto(succeededItemDtos, failureItemDtos);
    }
}
