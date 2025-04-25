package com.develop.backend.insfraestructure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal")
public class WebController {

    @GetMapping("/")
    public String home() {
        return "payment";

    }

//    @GetMapping("/success")
//    public String success() {
//        return "payment successful";
//
//    }


    @GetMapping("/cancel")
    public String cancel() {
        return "payment canceled";

    }
}
