package com.arqivame.drive.infrastructure.file;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileGateway;
import com.arqivame.drive.domain.file.FileID;

@Component
public class DefaultFileGateway implements FileGateway {

    @Override
    public Optional<File> findById(FileID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

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

}
