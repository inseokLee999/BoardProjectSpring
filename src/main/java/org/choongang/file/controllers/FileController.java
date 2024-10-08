package org.choongang.file.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.services.*;
import org.choongang.global.Utils;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.global.exceptions.RestExceptionProcessor;
import org.choongang.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements RestExceptionProcessor {

    private final FileUploadService uploadService;
    private final FileDownloadService downloadService;
    private final FileInfoService infoService;
    private final FileDeleteService deleteService;
    private final BeforeFileUploadProcess beforeProcess;
    private final AfterFileUploadProcess afterProcess;

    private final Utils utils;

    @PostMapping("/upload")//파일은 post 형태로 넘어온다
    public ResponseEntity<JSONData> upload(@RequestPart("file") MultipartFile[] files,//어떤 필드 인지 알려주는 역할(name값)
                                           @Valid RequestUpload form, Errors errors) {
        form.setFiles(files);

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            throw new BadRequestException(utils.getErrorMessages(errors));
        }
        beforeProcess.process(form);//파일 업로드 전처리
        List<FileInfo> items = uploadService.upload(files, form.getGid(), form.getLocation());

        afterProcess.process(form);//파일 업로드 후처리
        HttpStatus status = HttpStatus.CREATED;
        JSONData data = new JSONData(items);
        data.setStatus(status);

        return ResponseEntity.status(status).body(data);
    }

    @GetMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq) {
        downloadService.download(seq);
    }

    @DeleteMapping("/delete/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {
        FileInfo data = deleteService.delete(seq);

        return new JSONData(data);
    }

    @DeleteMapping("/deletes/{gid}")
    public JSONData deletes(@PathVariable("gid") String gid, @RequestParam(name = "location", required = false) String location) {//gid 는 필수 location는 필수는 아님
        List<FileInfo> items = deleteService.delete(gid, location);

        return new JSONData(items);
    }

    @GetMapping("/info/{seq}")
    public JSONData get(@PathVariable("seq") Long seq) {
        FileInfo data = infoService.get(seq);

        return new JSONData(data);
    }

    @GetMapping("/list/{gid}")
    public JSONData get(@PathVariable("gid") String gid, @RequestParam(name = "location", required = false) String location) {
        List<FileInfo> items = infoService.getList(gid, location);

        return new JSONData(items);
    }
}
