package com.nova.moviecatalogservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Intro {
    @GetMapping("/")
    public String greeting() {
        return "movie-catalog-service";
    }
}
