package com.example.nasa_apod_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.nasa_apod_backend.model.ApodResponse;

@Service
public class NasaApodService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${nasa.api.url}")
    private String apiUrl;

    @Value("${nasa.api.key}")
    private String apiKey;

    public ApodResponse getAstronomyPictureOfTheDay() {
        String url = apiUrl + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, ApodResponse.class);
    }
}
