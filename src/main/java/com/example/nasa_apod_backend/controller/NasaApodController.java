package com.example.nasa_apod_backend.controller;

import com.example.nasa_apod_backend.service.NasaApodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@RestController
public class NasaApodController {

    @Autowired
    private NasaApodService nasaApodService;

    /**
     * Gets the image of the day specified by date or the most recent image if no date is provided.
     * @param date Date in YYYY-MM-DD format (optional)
     * @return JSON from NASA API response
     */
    @GetMapping("/apod")
    public String getApod(@RequestParam(value = "date", required = false) String date) {
        if (date == null || date.isEmpty()) {
            date = LocalDate.now().toString();
        } else {
            validateDateFormat(date);
        }
        return nasaApodService.getApodByDate(date);
    }

    /**
     * Gets the images within a date range.
	 * @param startDate Start date in YYYY-MM-DD format (required)
	 * @param endDate End date in YYYY-MM-DD format (optional, defaults to current date)
	 * @return JSON from NASA API response
     */
    @GetMapping("/apods")
    public String getApodsByRange(@RequestParam("start_date") String startDate,
                                  @RequestParam(value = "end_date", required = false) String endDate) {
        if (endDate == null || endDate.isEmpty()) {
            endDate = LocalDate.now().toString();
        }

        validateDateFormat(startDate);
        validateDateFormat(endDate);

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        long daysBetween = ChronoUnit.DAYS.between(start, end);
        if (daysBetween > 6) {
            throw new IllegalArgumentException("El rango no puede exceder los 6 días.");
        }

        if (daysBetween < 0) {
            throw new IllegalArgumentException("La fecha final debe ser después de la fecha inicial.");
        }

        return nasaApodService.getApodsByRange(startDate, endDate);
    }


    /**
     * Validates that the date is in YYYY-MM-DD format.
     * @param date Date in YYYY-MM-DD format
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
