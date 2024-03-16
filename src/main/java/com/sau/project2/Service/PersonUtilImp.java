package com.sau.project2.Service;

import com.sau.project2.Entity.Person;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PersonUtilImp implements PersonUtil {
    public static String UPLOAD_DIRECTORY = "src/main/resources/static/images";

    @Override
    public void saveImage(Person person, MultipartFile file) throws IOException {
        //String fileName = person.getId().toString() + "." + this.extractImageFileType(file) ;
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIRECTORY, fileName);
        Files.write(path, file.getBytes());
    }

    @Override
    public String getImageUrl(Person person) {
        String img_url = person.getImg_url();
        int last_index = img_url.lastIndexOf("/");
        String x = "\\"+"images"+"\\";
        return  x + img_url.substring(last_index+1);
    }

    @Override
    public void deleteImage(Person person) throws IOException {
        File imageFile = new File(this.getPersonImageFileRealPath(person));
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }

    private String extractImageFileType(MultipartFile file) {
        String fileNameWithType = file.getOriginalFilename();
        String fileExtension = null;
        if (fileNameWithType != null) {
            int lastDotIndex = fileNameWithType.lastIndexOf('.');
            if (lastDotIndex > 0) {
                fileExtension = fileNameWithType.substring(lastDotIndex + 1);
            }
        }
        return fileExtension;
    }

    private String getPersonImageFileRealPath(Person person) throws IOException {
        Path imagePath = Paths.get(UPLOAD_DIRECTORY, person.getId().toString());
        return imagePath + "." + person.getImg_url();
    }

}
