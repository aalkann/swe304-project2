package com.sau.project2.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sau.project2.ImageUtils.ImageStorageStrategy;
import com.sau.project2.ImageUtils.ImageStorageStrategyFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sau.project2.Entity.Person;
import com.sau.project2.Service.PersonService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(path = "/person")
public class PersonController {


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
        }

        // Update Person
        String img_url = imageUtil.saveImage(imageFile);
        person.setImg_url(img_url);
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
