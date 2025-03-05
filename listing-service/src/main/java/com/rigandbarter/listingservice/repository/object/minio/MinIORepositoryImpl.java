package com.rigandbarter.listingservice.repository.object.minio;

import com.rigandbarter.core.util.MinIOFileUtil;
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
        return MinIOFileUtil.uploadFile(minioClient, LISTING_IMAGES_BUCKET, key, file, MINIO_URL);
    }

    @Override
    public void deleteFile(String key) {
        MinIOFileUtil.removeFile(minioClient, LISTING_IMAGES_BUCKET, key, MINIO_URL);
    }
}