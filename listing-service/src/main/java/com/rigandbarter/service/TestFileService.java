package com.rigandbarter.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(value = "rb.storage.file", havingValue = "test")
public class TestFileService implements IFileService {

    @Override
    public String uploadFile(MultipartFile file) {
        System.out.println("USED TEST FILE SERVICE");
        return null;
    }
}
