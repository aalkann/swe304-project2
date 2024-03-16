package com.sau.project2.Service;
import com.sau.project2.Entity.Person;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PersonUtil {
    void saveImage(Person person, MultipartFile file) throws IOException;

    String getImageUrl(Person person);

    void deleteImage(Person person) throws IOException;

}
