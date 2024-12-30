package com.example.nasa_apod_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class NasaApodService {

    private final WebClient webClient;

    @Value("${nasa.apod.api-key}")
    private String apiKey;

    public NasaApodService(@Value("${nasa.apod.base-url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Obtiene la imagen del día actual con almacenamiento en caché para evitar múltiples solicitudes a la API.
     * Incluye lógica de reintento en caso de errores temporales.
     * @return Respuesta de la API como String.
     */
    @Cacheable("todayApod")
    public String getTodayApod() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .block();
    }

    /**
     * Obtiene la imagen de un día específico.
     * Incluye lógica de reintento en caso de errores temporales.
     * @param date Fecha en formato YYYY-MM-DD.
     * @return Respuesta de la API como String.
     */
    public String getApodByDate(String date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key", apiKey)
                        .queryParam("date", date)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .block();
    }

    /**
     * Obtiene imágenes en un rango de fechas.
     * Incluye lógica de reintento en caso de errores temporales.
     * @param startDate Fecha inicial en formato YYYY-MM-DD.
     * @param endDate Fecha final en formato YYYY-MM-DD.
     * @return Respuesta de la API como String.
     */
    public String getApodsByRange(String startDate, String endDate) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key", apiKey)
                        .queryParam("start_date", startDate)
                        .queryParam("end_date", endDate)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                .block();
    }

}
