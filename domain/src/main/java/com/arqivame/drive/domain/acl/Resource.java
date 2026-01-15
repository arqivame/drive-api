package com.arqivame.drive.domain.acl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.arqivame.drive.domain.Identifier;
import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.exception.ValidationException;
import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileID;
import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.ValidationHandler;

public class Resource<I extends Identifier<?>> implements ValueObject {

    private final I id;
    private final Resource.Type type;
    private final UserID owner;

    private Resource(
            final I id,
            final Type type,
            final UserID owner) {
        this.id = id;
        this.type = type;
        this.owner = owner;
    }

    public Resource<FolderID> folder() {
        if (!Resource.Type.FOLDER.equals(this.type))
            throw ValidationException.with(
                    "Resource is not of type FOLDER",
                    ValidationError.with("'type' must be FOLDER when calling folder()"));

        return new Resource<>((FolderID) this.id, this.type, this.owner);
    }

    public Resource<FileID> file() {
        if (!Resource.Type.FILE.equals(this.type))
            throw ValidationException.with(
                    "Resource is not of type FILE",
                    ValidationError.with("'type' must be FILE when calling file()"));

        return new Resource<>((FileID) this.id, this.type, this.owner);
    }

    public static Resource<FolderID> folder(final Folder folder) {
        return new Resource<>(folder.getId(), Resource.Type.FOLDER, folder.getOwner());
    }

    public static Resource<FileID> file(final File file) {
        return new Resource<>(file.getId(), Resource.Type.FILE, file.getOwner());
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(id))
            handler.append(ValidationError.with("'id' cannot be null"));

        if (isNull(type))
            handler.append(ValidationError.with("'type' cannot be null"));

        if (nonNull(id))
            id.validate(handler);

    }

    public enum Type {
        FILE,
        FOLDER
    }

    public I getId() {
        return id;
    }

    public Resource.Type getType() {
        return type;
    }

    public UserID getOwner() {
        return owner;
    }

}
