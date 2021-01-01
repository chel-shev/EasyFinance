package ru.ixec.easyfinance.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloWorldController {

    @RequestMapping("protected/dashboard")
    public String secondPage() {
        return "success";
    }

    @RequestMapping("/dashboard")
    public String firstPage() {
        return "success";
    }
}
