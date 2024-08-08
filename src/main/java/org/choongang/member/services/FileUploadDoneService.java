package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.constants.FileStatus;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.choongang.file.services.FileInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadDoneService {
    private final FileInfoRepository repository;
    private final FileInfoService infoService;

    public void process(String gid, String location) {
        List<FileInfo> items = infoService.getList(gid, location, FileStatus.ALL);
        items.forEach(item -> item.setDone(true));
        repository.saveAllAndFlush(items);
    }

    public void process(String gid) {
        process(gid, null);
    }
}
