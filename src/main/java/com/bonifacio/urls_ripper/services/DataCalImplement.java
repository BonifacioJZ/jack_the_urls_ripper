package com.bonifacio.urls_ripper.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataCalImplement implements DateCal {
    /**
     * The function returns the expiration date as a LocalDateTime object, either by
     * parsing the
     * provided expiration date string or by adding one hour to the creation date if
     * the expiration
     * date is blank.
     *
     * @param expirationDate A string representing the expiration date in the format
     *                       "yyyy-MM-dd
     *                       HH:mm:ss".
     * @param creationData   The creationData parameter is a LocalDateTime object
     *                       representing the date
     *                       and time of creation.
     * @return The method is returning a LocalDateTime object.
     */
    @Override
    public LocalDateTime getExpirationData(String expirationDate, LocalDateTime creationData) {
        if (StringUtils.isBlank(expirationDate)) {
            return creationData.plusHours(1);
        }
        return LocalDateTime.parse(expirationDate);
    }

}
