package com.sau.project2.Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sau.project2.Service.PersonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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


    @Autowired
    private PersonService personService;

    @Autowired
    private PersonUtil personUtil;

    @GetMapping("")
    public String ListPeople(Model model) throws IOException {
        // Get People
        List<Person> people = personService.getAll();

        // Get People Images
        List<String> imageUrls = new ArrayList<>();
        for (Person person : people) {
            String imagePath = personUtil.getImageUrl(person);
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
        // Save Person
        Person personSaved = personService.save(person);

        // Store Person Image Locally
        personUtil.saveImage(person, imageFile);

        // Redirect to Person List View
        return "redirect:/person";
    }

    @PostMapping("update")
    public String UpdatePerson(@ModelAttribute Person person, @RequestParam("image") MultipartFile imageFile) throws IOException {
        // Update Person
        Person updatedPerson = personService.update(person);

        // If Image Is Not Empty
        if (!imageFile.isEmpty()) {
            // Update Person Image
            personUtil.deleteImage(updatedPerson);
            personUtil.saveImage(updatedPerson, imageFile);
        }

        // Redirect to Person List View
        return "redirect:/person";
    }

    @PostMapping("delete")
    public String DeletePerson(@RequestParam("id") Long id) throws IOException {
        // Delete Person
        Person person = personService.getById(id);
        personService.delete(id);

        // Delete Person Image
        personUtil.deleteImage(person);

        // Redirect to Person List View
        return "redirect:/person";
    }


}
