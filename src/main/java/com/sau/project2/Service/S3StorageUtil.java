package com.sau.project2.Service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.sau.project2.Entity.Person;
import com.sau.project2.ImageUtils.ImageStorageStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

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
            String key = file.getOriginalFilename();
            PutObjectResult result = s3Client.putObject(bucketName, key, file.getInputStream(), null);
            return s3Client.getUrl(bucketName, key).toString();

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            throw ioException;
        } catch (AmazonS3Exception s3Exception) {
            System.out.println(s3Exception.getMessage());
            throw s3Exception;
        }

    }


    public Optional<S3Object> getImage(Person person) {
        return Optional.ofNullable(s3Client.getObject(bucketName, person.getImg_url()));
    }

    @Override
    public String getImageFromURL(Person person) {
        S3Object image = s3Client.getObject(bucketName, getImageName(person.getImg_url()));

        S3ObjectInputStream objectContent = image.getObjectContent();
        try {


            String imageName = getImageName(person.getImg_url());


            Path imagePath = Paths.get("src/main/resources/static/images", imageName);
            Files.createDirectories(imagePath.getParent());
            Files.copy(objectContent, imagePath, StandardCopyOption.REPLACE_EXISTING);


            return getImageLocalPath(person);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public String getImageLocalPath(Person person) {
        String url = person.getImg_url();
        int last_index = url.lastIndexOf("/");
        return "/images" + File.separator + url.substring(last_index + 1);
    }


    @Override
    public void deleteImage(Person person) {
        try {
            s3Client.deleteObject(bucketName, getImageName(person.getImg_url()));
            File imageFile = new File("src/main/resources/static/images" + getImageName(person.getImg_url()));
            if (imageFile.exists()) {
                imageFile.delete();
            }
        } catch (AmazonS3Exception s3Exception) {
            System.out.println(s3Exception.getMessage());
        }
    }


    @Override
    public String getImageName(String img_url) {
        return img_url.substring(img_url.lastIndexOf("/") + 1);
    }
}

