package org.test.ticketingservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ticketing-service")
public class ApplicationProperties {

    private String venueMinLevel;
    private String venueMaxLevel;
    private String holdDurationSeconds;

    public String getVenueMinLevel() {
        return venueMinLevel;
    }

    public void setVenueMinLevel(String venueMinLevel) {
        this.venueMinLevel = venueMinLevel;
    }

    public String getVenueMaxLevel() {
        return venueMaxLevel;
    }

    public void setVenueMaxLevel(String venueMaxLevel) {
        this.venueMaxLevel = venueMaxLevel;
    }

    public String getHoldDurationSeconds() {
        return holdDurationSeconds;
    }

    public void setHoldDurationSeconds(String holdDurationSeconds) {
        this.holdDurationSeconds = holdDurationSeconds;
    }
}
