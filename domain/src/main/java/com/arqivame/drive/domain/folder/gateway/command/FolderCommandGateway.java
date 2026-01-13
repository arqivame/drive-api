package com.arqivame.drive.domain.folder.gateway.command;

import com.arqivame.drive.domain.folder.Folder;

public interface FolderCommandGateway {

    Folder create(Folder folder);

    Folder update(Folder folder);

}
