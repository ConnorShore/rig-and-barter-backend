package com.rigandbarter.repository.file;

import org.springframework.web.multipart.MultipartFile;

public interface IFileRepository {

    /**
     * Uploads a file to file/blob storage
     * @param file The file to upload
     * @return The id/url of the uploaded file
     */
    String uploadFile(String key, MultipartFile file);
}
