package com.sau.project2.Service;

import com.sau.project2.Entity.Person;
import com.sau.project2.ImageUtils.ImageStorageStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalStorageUtil implements ImageStorageStrategy {

    public final String UPLOAD_DIRECTORY = "src/main/resources/static/images";

    @Override

    public String saveImage(MultipartFile file) throws IOException {
        //String fileName = person.getId().toString() + "." + this.extractImageFileType(file) ;
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIRECTORY, fileName);
        Files.write(path, file.getBytes());
        return path.toString();
    }


    @Override
    public String getImageFromURL(Person person) {
        String img_url = person.getImg_url();
        int last_index = img_url.lastIndexOf("\\");
        String prefix = "\\" + "images" + "\\";
        return prefix + img_url.substring(last_index + 1);
    }

    @Override
    public void deleteImage(Person person) throws IOException {
        File imageFile = new File(this.getPersonImageFileRealPath(person));
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }


    private String getPersonImageFileRealPath(Person person) throws IOException {
        Path imagePath = Paths.get(UPLOAD_DIRECTORY, person.getId().toString());
        return imagePath + "." + person.getImg_url();
    }

    @Override
    public String getImageName(String img_url) {
        return img_url.substring(img_url.lastIndexOf("/") + 1);
    }
}
