package com.sau.project2.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sau.project2.ImageUtils.ImageStorageStrategy;
import com.sau.project2.ImageUtils.ImageStorageStrategyFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sau.project2.Entity.Person;
import com.sau.project2.Service.PersonService;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(path = "/person")
public class PersonController  {


    private final PersonService personService;
    private final ImageStorageStrategy imageUtil;


    public PersonController(ImageStorageStrategyFactory imageUtilFactory, PersonService personService) {
        this.imageUtil = imageUtilFactory.getImageStorageStrategy();
        this.personService = personService;
    }

    @GetMapping("")
    public String ListPeople(Model model) throws IOException {
        // Get People
        List<Person> people = personService.getAll();

        // Get People Images
        List<String> imageUrls = new ArrayList<>();
        for (Person person : people) {
            String imagePath = imageUtil.getImageFromURL(person);

            imageUrls.add(imagePath);
        }

        // Add Object into The Model
        model.addAttribute("people", people);
        model.addAttribute("imageUrls", imageUrls);

        // Return Person List View
        return "Person/index.html";
    }


    @PostMapping("add")
    public String AddPerson(@ModelAttribute Person person, @RequestParam("image") MultipartFile imageFile) throws IOException {

        // Store Image
        String img_url = imageUtil.saveImage(imageFile);

        // Save Person
        person.setImg_url(img_url);
        Person personSaved = personService.save(person);


        // Redirect to Person List View
        return "redirect:/person";
    }

    @PostMapping("update")
    public String UpdatePerson(@ModelAttribute Person person, @RequestParam("image") MultipartFile imageFile) throws IOException {


        // If Image Is Not Empty
        if (!imageFile.isEmpty()) {
            // Delete Old Image
            imageUtil.deleteImage(person);

            // Update Person Image
            String img_url = imageUtil.saveImage(imageFile);
            person.setImg_url(img_url);
        }
        // Keep the same image but update the other fields
        else
        {
            String old_img_url = personService.getById(person.getId()).getImg_url();
            person.setImg_url(old_img_url);
        }
        personService.update(person);


        // Redirect to Person List View
        return "redirect:/person";
    }

    @PostMapping("delete")
    public String DeletePerson(@RequestParam("id") Long id) throws IOException {
        // Delete Person
        Person person = personService.getById(id);
        personService.delete(id);

        // Delete Person Image
        imageUtil.deleteImage(person);

        // Redirect to Person List View
        return "redirect:/person";
    }




}
