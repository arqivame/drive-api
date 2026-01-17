package com.arqivame.drive.domain.folder.gateway.query;

import java.util.Optional;
import java.util.Set;

import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;

public interface FolderQueryGateway {

    Optional<Folder> findRootByOwner(UserID owner);

    Optional<Folder> findVisibleById(FolderID id, UserID actor);

    Set<Folder> findByParentFolderId(FolderID parentFolderId);

    Set<Folder> findVisibleByParentFolderId(FolderID parentFolderId, UserID actor);

}
