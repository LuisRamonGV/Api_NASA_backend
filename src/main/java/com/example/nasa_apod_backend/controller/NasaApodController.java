package com.example.nasa_apod_backend.controller;

import com.example.nasa_apod_backend.service.NasaApodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
public class NasaApodController {

    @Autowired
    private NasaApodService nasaApodService;

    /**
     * Obtiene la imagen del día especificada por la fecha o la imagen más reciente si no se proporciona fecha.
     * @param date Fecha en formato YYYY-MM-DD (opcional)
     * @return JSON de la respuesta de la API de NASA
     */
    @GetMapping("/apod")
    public String getApod(@RequestParam(value = "date", required = false) String date) {
        // Usa la fecha actual si no se proporciona o valida el formato
        if (date == null || date.isEmpty()) {
            date = LocalDate.now().toString(); // Fecha actual
        } else {
            validateDateFormat(date); // Valida el formato de la fecha
        }
        return nasaApodService.getApodByDate(date);
    }

    /**
     * Obtiene las imágenes dentro de un rango de fechas.
     * @param startDate Fecha de inicio en formato YYYY-MM-DD (requerido)
     * @param endDate Fecha de fin en formato YYYY-MM-DD (opcional, por defecto la fecha actual)
     * @return JSON de la respuesta de la API de NASA
     */
    @GetMapping("/apods")
    public String getApodsByRange(@RequestParam("start_date") String startDate,
                                  @RequestParam(value = "end_date", required = false) String endDate) {
        // Usa la fecha actual si no se proporciona endDate
        if (endDate == null || endDate.isEmpty()) {
            endDate = LocalDate.now().toString(); // Fecha actual
        }
        // Valida el formato de las fechas
        validateDateFormat(startDate);
        validateDateFormat(endDate);
        return nasaApodService.getApodsByRange(startDate, endDate);
    }

    /**
     * Valida que la fecha esté en el formato YYYY-MM-DD.
     * @param date Fecha en formato YYYY-MM-DD.
     */
    private void validateDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha inválida: debe estar en el formato YYYY-MM-DD.");
        }
    }
}
