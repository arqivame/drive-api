package com.arqivame.drive.domain.file.gateway.command;

import com.arqivame.drive.domain.file.File;

public interface FileCommandGateway {

    File create(File file);

    File update(File file);

}
