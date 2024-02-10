package com.rigandbarter.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    /**
     * Uploads a file to file/blob storage
     * @param file The file to upload
     * @return The id/url of the uploaded file
     */
    String uploadFile(MultipartFile file);
}
