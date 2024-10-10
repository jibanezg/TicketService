package org.test.ticketingservice.controller.validation;

import org.springframework.stereotype.Component;
import org.test.ticketingservice.config.ApplicationProperties;

import java.util.Optional;

/**
 * Venue level validator class serves as an input level validator to ensure
 * the client has sent the right parameters when consuming the
 */
@Component
public class VenueLevelValidator {

    private final int venueMinLevel;
    private final int venueMaxLevel;

    public VenueLevelValidator(ApplicationProperties applicationProperties){
        this.venueMinLevel = Integer.parseInt(applicationProperties.getVenueMinLevel());
        this.venueMaxLevel = Integer.parseInt(applicationProperties.getVenueMaxLevel());
    }

    public boolean isValidVenueLevel(Integer venueLevel){

        return venueLevel >= this.venueMinLevel && venueLevel <= this.venueMaxLevel;
    }

    public boolean isValidVenueLevelRange(Optional<Integer> venueMinLevel, Optional<Integer> venueMaxLevel){
        return isValidVenueLevel(venueMinLevel.orElse(this.venueMinLevel)) &&  isValidVenueLevel(venueMaxLevel.orElse(this.venueMaxLevel));
    }

}
