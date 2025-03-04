package com.rigandbarter.listingservice.repository.object.minio;

import com.rigandbarter.listingservice.repository.object.IObjectRepository;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@ConditionalOnProperty(value = "rb.storage.file", havingValue = "minio")
@Slf4j
public class MinIORepositoryImpl implements IObjectRepository {

    @Value("${minio.url}")
    private String MINIO_URL;

    @Value("${minio.bucket-name}")
    private String LISTING_IMAGES_BUCKET;

    @Autowired
    private MinioClient minioClient;

    @Override
    public String uploadFile(String key, MultipartFile file) {
        log.info("Uploading file to MinIO {}", key);
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(LISTING_IMAGES_BUCKET)
                    .build()
            );

            if (!found) {
                log.error("No bucket found with name {}", LISTING_IMAGES_BUCKET);
                return null;
            }

            // Upload the file to MinIO
            var putObjectArgs = PutObjectArgs.builder()
                    .bucket(LISTING_IMAGES_BUCKET)
                    .object(key)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
            log.info("Successfully uploaded file to MinIO");

            // Return the public url of the uploaded file
            return String.format("%s/%s/%s", MINIO_URL, LISTING_IMAGES_BUCKET, key);

        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            return null;
        }
    }
}