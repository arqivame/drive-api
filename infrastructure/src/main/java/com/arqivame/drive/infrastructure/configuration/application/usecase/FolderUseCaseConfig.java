package com.arqivame.drive.infrastructure.configuration.application.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.drive.application.service.folder.FolderProvisioningService;
import com.arqivame.drive.application.usecase.folder.retrieve.get.root.DefaultGetRootFolderUseCase;
import com.arqivame.drive.application.usecase.folder.retrieve.get.root.GetRootFolderUseCase;
import com.arqivame.drive.domain.file.gateway.query.FileQueryGateway;
import com.arqivame.drive.domain.folder.gateway.query.FolderQueryGateway;

@Configuration
public class FolderUseCaseConfig {

    private final FolderQueryGateway folderQueryGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FolderProvisioningService folderProvisioningService;

    public FolderUseCaseConfig(
            final FolderQueryGateway folderQueryGateway,
            final FileQueryGateway fileQueryGateway,
            final FolderProvisioningService folderProvisioningService) {
        this.folderQueryGateway = folderQueryGateway;
        this.fileQueryGateway = fileQueryGateway;
        this.folderProvisioningService = folderProvisioningService;
    }

    @Bean
    GetRootFolderUseCase getRootFolderUseCase() {
        return new DefaultGetRootFolderUseCase(folderQueryGateway, fileQueryGateway, folderProvisioningService);
    }

}
