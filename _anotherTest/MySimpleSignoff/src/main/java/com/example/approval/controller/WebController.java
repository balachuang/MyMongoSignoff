package com.example.approval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "list";
    }

    @GetMapping("/form/new")
    public String createForm() {
        return "form";
    }

    @GetMapping("/form/edit/{id}")
    public String editForm(@PathVariable Long id) {
        return "form";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id) {
        return "detail";
    }
}
