package com.arqivame.drive.infrastructure.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arqivame.drive.application.usecase.file.create.CreateFileInput;
import com.arqivame.drive.application.usecase.file.create.CreateFileUseCase;
import com.arqivame.drive.application.usecase.folder.retrieve.get.root.GetRootFolderInput;
import com.arqivame.drive.application.usecase.folder.retrieve.get.root.GetRootFolderOutput;
import com.arqivame.drive.application.usecase.folder.retrieve.get.root.GetRootFolderUseCase;
import com.arqivame.drive.infrastructure.file.model.CreateFileResponse;

@RestController
@RequestMapping("test")
public class TestController {

    private final CreateFileUseCase createFileUseCase;
    private final GetRootFolderUseCase getRootFolderUseCase;

    public TestController(CreateFileUseCase createFileUseCase, GetRootFolderUseCase getRootFolderUseCase) {
        this.createFileUseCase = createFileUseCase;
        this.getRootFolderUseCase = getRootFolderUseCase;
    }

    @GetMapping("folders/root")
    public GetRootFolderOutput getRootFolder(@RequestParam("userId") UUID userId) {
        return getRootFolderUseCase.execute(GetRootFolderInput.from(userId));
    }

    @PostMapping("files")
    public ResponseEntity<CreateFileResponse> createFile(@RequestBody CreateFileInput input) {

        var output = createFileUseCase.execute(input);

        return ResponseEntity.ok(new CreateFileResponse(output.id()));
    }

}
