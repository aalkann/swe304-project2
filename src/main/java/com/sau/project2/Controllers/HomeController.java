package com.sau.project2.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("")
    public String Index() {
        return "index.html";
    }

}
