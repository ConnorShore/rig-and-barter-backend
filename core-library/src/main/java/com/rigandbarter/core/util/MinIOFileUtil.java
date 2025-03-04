package com.rigandbarter.core.util;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class MinIOFileUtil {

    /**
     * Uploads a file to MinIO
     * @param minioClient The MinIO client
     * @param bucketName The name of the bucket to upload the file to
     * @param key The key to save the file as
     * @param file The file to upload
     * @param minioUrl The URL of the MinIO server
     * @return The public URL of the uploaded file
     */
    public static String uploadFile(MinioClient minioClient, String bucketName, String key, MultipartFile file, String minioUrl) {
        log.info("Uploading file to MinIO {}", key);
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );

            if (!found) {
                log.error("No bucket found with name {}", bucketName);
                return null;
            }

            // Upload the file to MinIO
            var putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
            log.info("Successfully uploaded file to MinIO");

            // Return the public url of the uploaded file
            return String.format("%s/%s/%s", minioUrl, bucketName, key);
        } catch (Exception e) {
            log.error("Error uploading file [{}] to MinIO", key, e);
            return null;
        }
    }

    public static void removeFile(MinioClient minioClient, String bucketName, String key, String minioUrl) {
        log.info("Removing file to MinIO {}", key);
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );

            if (!found) {
                log.error("No bucket found with name {}", bucketName);
                return;
            }

            //Remove file from minIO
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .build());
            log.info("Successfully removed file [{}] from MinIO", key);
        } catch (Exception e) {
            log.error("Error removing file [{}] from MinIO", key, e);
        }
    }
}
