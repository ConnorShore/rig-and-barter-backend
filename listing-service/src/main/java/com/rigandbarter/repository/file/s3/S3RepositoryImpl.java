package com.rigandbarter.repository.file.s3;

import com.rigandbarter.repository.file.IFileRepository;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import java.io.IOException;

@Repository
@ConditionalOnProperty(value = "rb.storage.file", havingValue = "aws-s3")
public class S3RepositoryImpl implements IFileRepository {

    @Autowired
    private S3Template s3Template;

    @Value("${aws.s3.bucket-name}")
    private String LISTING_IMAGES_BUCKET;

    @Override
    public String uploadFile(String key, MultipartFile file) {
        // Set metadata
        var metaData = new ObjectMetadata.Builder()
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        // Save to S3
        String resourceUrl = null;
        try {
            var resource = s3Template.upload(LISTING_IMAGES_BUCKET, key, file.getInputStream(), metaData);
            resourceUrl = resource.getURL().toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An exception occurred when uploading listing image to S3");
        }

        // Return s3 resource url
        return resourceUrl;
    }
}