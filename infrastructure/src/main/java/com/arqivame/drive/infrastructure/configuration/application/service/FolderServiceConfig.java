package com.arqivame.drive.infrastructure.configuration.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.drive.application.service.folder.FolderProvisioningService;
import com.arqivame.drive.domain.acl.AclGateway;
import com.arqivame.drive.domain.event.EventDispatcher;
import com.arqivame.drive.domain.folder.gateway.command.FolderCommandGateway;
import com.arqivame.drive.domain.folder.gateway.query.FolderQueryGateway;

@Configuration
public class FolderServiceConfig {

    private final EventDispatcher eventDispatcher;
    private final FolderQueryGateway folderQueryGateway;
    private final FolderCommandGateway folderCommandGateway;
    private final AclGateway aclGateway;

    public FolderServiceConfig(
            final EventDispatcher eventDispatcher,
            final FolderQueryGateway folderQueryGateway,
            final FolderCommandGateway folderCommandGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = eventDispatcher;
        this.folderQueryGateway = folderQueryGateway;
        this.folderCommandGateway = folderCommandGateway;
        this.aclGateway = aclGateway;
    }

    @Bean
    FolderProvisioningService folderProvisioningService() {
        return new FolderProvisioningService(
                eventDispatcher,
                folderQueryGateway,
                folderCommandGateway,
                aclGateway);
    }

}
