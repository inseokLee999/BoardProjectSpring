package org.choongang.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/")
@Controller
public class MainController {
    @GetMapping
    public String index(Model model) {

        model.addAttribute("addCommonScript", List.of("fileManager"));
        model.addAttribute("addScript", List.of("test/form"));

        return "front/main/index";
    }
}
