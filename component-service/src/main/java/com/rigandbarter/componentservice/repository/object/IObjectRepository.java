package com.rigandbarter.componentservice.repository.object;

import org.springframework.web.multipart.MultipartFile;

public interface IObjectRepository {

    /**
     * Uploads a file to file/blob storage
     * @param key The key to save the file as
     * @param file The file to upload
     * @return The id/url of the uploaded file
     */
    String uploadFile(String key, MultipartFile file);
}
