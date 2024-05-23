package com.rigandbarter.userservice.repository.file;

import org.springframework.web.multipart.MultipartFile;

public interface IProfilePictureRepository {

    /**
     * Uploads a profile picture to blob/file storage
     * @param file The profile picture to upload
     * @return The id/url of the uploaded file
     * TODO: Maybe need to make return object so can get both id and url
     */
    String uploadProfilePicture(String key, MultipartFile file);

    /**
     * Removes a file from the blob/file storage
     * @param key The key of the file to remove
     */
    void removeFile(String key);
}
