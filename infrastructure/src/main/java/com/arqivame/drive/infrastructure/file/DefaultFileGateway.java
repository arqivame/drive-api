package com.arqivame.drive.infrastructure.file;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileID;
import com.arqivame.drive.domain.file.FileName;
import com.arqivame.drive.domain.file.gateway.command.FileCommandGateway;
import com.arqivame.drive.domain.file.gateway.query.FileQueryGateway;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.member.MemberID;

@Component
public class DefaultFileGateway implements FileQueryGateway, FileCommandGateway {

    @Override
    public File create(File file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public File update(File file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Optional<File> findById(FileID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Boolean existsFileWithNameInFolder(FileName name, FolderID folderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsFileWithNameInFolder'");
    }

    @Override
    public List<File> listFilesInFolder(FolderID folderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listFilesInFolder'");
    }

    @Override
    public Long totalBytesUsedBy(MemberID owner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'totalBytesUsedBy'");
    }

}
