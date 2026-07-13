package fr.bastienbories.discorddrivesync.drive.mapper;

import fr.bastienbories.discorddrivesync.drive.model.DriveFile;
import fr.bastienbories.discorddrivesync.drive.model.dto.DriveFileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriveFileMapper {
//    @Mapping(target = "id", source = "idFile")
    DriveFileDto driveFileToDriveFileDto(DriveFile driveFile);
}
