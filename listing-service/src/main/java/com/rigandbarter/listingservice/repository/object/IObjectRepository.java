package com.rigandbarter.listingservice.repository.object;

import org.springframework.web.multipart.MultipartFile;

public interface IObjectRepository {

    /**
     * Uploads a file to object storage
     * @param file The file to upload
     * @return The id/url of the uploaded file
     */
    String uploadFile(String key, MultipartFile file);

    /**
     * Delete file from object storage
     * @param key The id of the file to remove
     */
    void deleteFile(String key);
}
