package com.example.nasa_apod_backend.controller;

import com.example.nasa_apod_backend.model.ApodResponse;
import com.example.nasa_apod_backend.service.NasaApodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NasaApodController {

    @Autowired
    private NasaApodService nasaApodService;

    @GetMapping("/apod")
    public ApodResponse getApod() {
        return nasaApodService.getAstronomyPictureOfTheDay();
    }
}
