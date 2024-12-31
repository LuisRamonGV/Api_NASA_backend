package com.example.nasa_apod_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class NasaApodService {

    private final WebClient webClient;

    @Value("${nasa.apod.api-key}")
    private String apiKey;

    private final Map<String, CachedResponse> cache = new HashMap<>();

    public NasaApodService(@Value("${nasa.apod.base-url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public String getTodayApod() {
        String todayKey = "today";

        if (cache.containsKey(todayKey) && !cache.get(todayKey).isExpired()) {
            return cache.get(todayKey).getData();
        }

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .block();

        cache.put(todayKey, new CachedResponse(response));
        return response;
    }

    public String getApodByDate(String date) {
        String cacheKey = "date-" + date;

        if (cache.containsKey(cacheKey) && !cache.get(cacheKey).isExpired()) {
            return cache.get(cacheKey).getData();
        }

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key", apiKey)
                        .queryParam("date", date)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .block();

        cache.put(cacheKey, new CachedResponse(response));
        return response;
    }

    public String getApodsByRange(String startDate, String endDate) {
        String cacheKey = "range-" + startDate + "-" + endDate;

        if (cache.containsKey(cacheKey) && !cache.get(cacheKey).isExpired()) {
            return cache.get(cacheKey).getData();
        }

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key", apiKey)
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .block();

        cache.put(cacheKey, new CachedResponse(response));
        return response;
    }

    /**
     * Inner class to manage cached data and its expiration.
     */
    private static class CachedResponse {
        private final String data;
        private final LocalDateTime expirationTime;

        private static final Duration CACHE_DURATION = Duration.ofMinutes(10);

        public CachedResponse(String data) {
            this.data = data;
            this.expirationTime = LocalDateTime.now().plus(CACHE_DURATION);
        }

        public String getData() {
            return data;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expirationTime);
        }
    }
}
