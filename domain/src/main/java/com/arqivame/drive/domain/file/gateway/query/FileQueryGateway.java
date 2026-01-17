package com.arqivame.drive.domain.file.gateway.query;

import java.util.List;
import java.util.Optional;

import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileID;
import com.arqivame.drive.domain.file.FileName;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;

public interface FileQueryGateway {

    Optional<File> findById(FileID id);

    Optional<File> findVisibleById(FileID id, UserID actor);

    Boolean existsFileWithNameInFolder(FileName name, FolderID folderId);

    List<File> findAllInFolder(FolderID folderId);

    Long totalBytesUsedBy(UserID owner);

}
