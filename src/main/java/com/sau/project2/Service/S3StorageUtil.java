package com.sau.project2.Service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.sau.project2.Entity.Person;
import com.sau.project2.ImageUtils.ImageStorageStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class S3StorageUtil implements ImageStorageStrategy {

    private final AmazonS3 s3Client;

    public S3StorageUtil(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        try {
            //Upload to cloud
            String key = file.getOriginalFilename();
            PutObjectResult result = s3Client.putObject(bucketName, key, file.getInputStream(), null);
            return key;
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            throw ioException;
        } catch (AmazonS3Exception s3Exception) {
            System.out.println(s3Exception.getMessage());
            throw s3Exception;
        }

    }


    public S3Object getImageFromS3(String key) {
        return s3Client.getObject(bucketName, key);
    }

    @Override
    public String getImagePathFromUrl(String url) {

        return generatePresignedUrl(url);
    }


    @Override
    public void deleteImage(Person person) {
        try {
            String imageName = getImageName(person.getImg_url());

            //Delete from cloud
            s3Client.deleteObject(bucketName, imageName);


        } catch (AmazonS3Exception s3Exception) {
            System.out.println(s3Exception.getMessage());
        }
    }


    @Override
    public String getImageName(String img_url) {
        return img_url.substring(img_url.lastIndexOf("/") + 1);
    }

    public String generatePresignedUrl(String key) {

        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        return s3Client.generatePresignedUrl(bucketName, key, expiration).toString();
    }
}

