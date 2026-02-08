package com.arqivame.drive.infrastructure.configuration.application.usecase;

import static java.util.Objects.requireNonNull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.drive.application.usecase.file.create.CreateFileUseCase;
import com.arqivame.drive.application.usecase.file.create.DefaultCreateFileUseCase;
import com.arqivame.drive.domain.acl.AclGateway;
import com.arqivame.drive.domain.event.EventDispatcher;
import com.arqivame.drive.domain.file.gateway.command.FileCommandGateway;
import com.arqivame.drive.domain.file.gateway.query.FileQueryGateway;
import com.arqivame.drive.domain.folder.gateway.query.FolderQueryGateway;
import com.arqivame.drive.domain.user.gateway.query.UserQueryGateway;

@Configuration
public class FileUseCaseConfig {

    private final EventDispatcher eventDispatcher;
    private final UserQueryGateway userGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final AclGateway aclGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FileCommandGateway fileCommandGateway;

    public FileUseCaseConfig(
            final EventDispatcher eventDispatcher,
            final UserQueryGateway userGateway,
            final FolderQueryGateway folderQueryGateway,
            final AclGateway aclGateway,
            final FileQueryGateway fileQueryGateway,
            final FileCommandGateway fileCommandGateway) {
        this.eventDispatcher = requireNonNull(eventDispatcher);
        this.userGateway = requireNonNull(userGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.aclGateway = requireNonNull(aclGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCommandGateway = requireNonNull(fileCommandGateway);
    }

    @Bean
    CreateFileUseCase createFileUseCase() {
        return new DefaultCreateFileUseCase(
                eventDispatcher,
                userGateway,
                folderQueryGateway,
                aclGateway,
                fileQueryGateway,
                fileCommandGateway);
    }

}
