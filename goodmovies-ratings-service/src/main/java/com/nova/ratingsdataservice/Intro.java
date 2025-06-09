package com.nova.ratingsdataservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Intro {
    @GetMapping("/")
    public String greeting() {
        return "goodmovies-ratings-data-service";
    }
}
