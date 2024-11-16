package com.koyeb.rayshan.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/")
    public String hello() {
        return String.format("Hello world second time!");
    }

    @GetMapping("/test2")
    public String test2() {
        return String.format("Hello world second time2!");
    }
}
