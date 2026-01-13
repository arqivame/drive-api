package com.arqivame.drive.domain.folder.gateway.query;

import java.util.Optional;

import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.member.MemberID;

public interface FolderQueryGateway {

    Optional<Folder> findVisibleById(FolderID id, MemberID actor);

}
