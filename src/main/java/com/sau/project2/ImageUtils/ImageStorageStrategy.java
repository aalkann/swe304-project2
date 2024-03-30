package com.sau.project2.ImageUtils;

import com.sau.project2.Entity.Person;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageStrategy {

    String saveImage(MultipartFile file) throws IOException;


    String getImageFromURL(Person person);

    void deleteImage(Person person) throws IOException;

    String getImageName(String img_url);

}
