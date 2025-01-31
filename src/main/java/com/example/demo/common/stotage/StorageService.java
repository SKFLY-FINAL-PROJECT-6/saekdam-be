package com.example.demo.common.stotage;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;
import java.net.URL;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public interface StorageService {
    List<String> generatePresignedUrls(List<String> uuids);
}

@Service
class S3StorageService implements StorageService {
    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public S3StorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<String> generatePresignedUrls(List<String> uuids) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < uuids.size(); i++) {
            urls.add(generatePresignedUrl(uuids.get(i)));
        }

        return urls;
    }

    private String generatePresignedUrl(String objectKey) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getExpirationTime());
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private Date getExpirationTime() {
        Date expiration = new Date();
        long expirationInMs = expiration.getTime();
        expirationInMs += 1000 * 60 * 30;
        expiration.setTime(expirationInMs);
        return expiration;
    }
}