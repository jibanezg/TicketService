package org.test.ticketingservice.service.impl;

import org.springframework.stereotype.Service;
import org.test.ticketingservice.config.ApplicationProperties;
import org.test.ticketingservice.domain.Seat;
import org.test.ticketingservice.domain.SeatHold;
import org.test.ticketingservice.cache.SeatHoldCache;
import org.test.ticketingservice.domain.VenueLevel;
import org.test.ticketingservice.exception.TicketServiceException;
import org.test.ticketingservice.repository.SeatRepository;
import org.test.ticketingservice.repository.VenueLevelRepository;
import org.test.ticketingservice.service.TicketService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TicketServiceImpl is the concrete implementation of the TicketService interface.
 * This class is composed of two repositories {@link VenueLevelRepository} and
 * {@link SeatRepository}.
 * <p>
 * THis service is in charge of providing the necessary functionality for:
 * - Getting the number of available seats in a level {@link this#numSeatsAvailable(Optional)}
 * - Finding seats in a range and creating a hold on them {@link this#findAndHoldSeats(int, Optional, Optional, String)}
 * - Reserving seats in an acquired seat hold {@link this#reserveSeats(UUID, String)}
 *
 * VenueLevel was designed as an aggregate root containing the properties of a level and
 * its associated children. It is through this domain entity that data like price, number of rows,
 * number of seats per rows and seats, should be accessed from. However, for simplicity's sake, I have
 * adapted my design to the provided contract {@link TicketService}. What this means is that instead
 * of using the {@link VenueLevelRepository} as the main data access entry point to manage both, venue level
 * data and seats, I created an additional repository so that seats can be easily updated. It is a trade-off
 * I decided to assume in order to reduce the complexity associated with updating the SeatStatus of
 * venue level seats.
 */
@Service
public class TicketServiceImpl implements TicketService {

    private final VenueLevelRepository venueLevelRepository;
    private final SeatRepository seatRepository;
    private final ApplicationProperties applicationProperties;
    private final SeatHoldCache seatHoldCache;

    private static final String RESERVATION_SUCCESS_RESPONSE_CODE = "Seats Reserved.";


    public TicketServiceImpl(VenueLevelRepository venueLevelRepository, SeatRepository seatRepository, ApplicationProperties applicationProperties, SeatHoldCache seatHoldCache) {
        this.venueLevelRepository = venueLevelRepository;
        this.seatRepository = seatRepository;
        this.applicationProperties = applicationProperties;
        this.seatHoldCache = seatHoldCache;
    }

    @Override
    public int numSeatsAvailable(Optional<Integer> levelId) {

        if(levelId.isEmpty()){
            throw new TicketServiceException("Venue level is mandatory.");
        }

        //Venue level for the requested level id is retrieved.
        Optional<VenueLevel> venueLevel = venueLevelRepository.findById(levelId.get());

        //Number of available seats is returned by leveraging on the venue level domain object
        //method getAvailableSeats
        return venueLevel.map(level -> level.getAvailableSeats().size()).orElse(0);

    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {

        //Venue levels and hold duration are defined first. These parameters were defined as configurable
        //properties for flexibility
        int minVenueLevel = Integer.parseInt(applicationProperties.getVenueMinLevel());
        int maxVenueLevel = Integer.parseInt(applicationProperties.getVenueMaxLevel());
        int holdDurationSeconds = Integer.parseInt(applicationProperties.getHoldDurationSeconds());

        //Venue levels are retrieved next. All the venue levels found in range are retrieved.
        //If no range is provided min and max are used
        List<VenueLevel> venueLevelsInRange = venueLevelRepository.findAvailableSeatsInVenueLevels(minLevel.orElse(minVenueLevel), maxLevel.orElse(maxVenueLevel));
        //All available seats in each venue level is flat mapped to a list of available seats.
        List<Seat> availableSeats = venueLevelsInRange.stream().flatMap(v -> v.getAvailableSeats().stream()).toList();

        //Business validation enforced. If the requested number of seats is greater than the number
        //of available seats an error is reported to the client.
        if(availableSeats.size() < numSeats || numSeats <= 0){
            throw new TicketServiceException("Not enough seats to complete the transaction or number of seats invalid");
        }

        //A hold is cached with the list of held seats.
        SeatHold seatHold = new SeatHold(customerEmail, availableSeats.stream().limit(numSeats).toList(), holdDurationSeconds);
        seatHoldCache.cacheSeatHold(seatHold);
        //Finally, the held seats are updated in the database.
        seatRepository.saveAll(seatHold.getHeldSeats());

        return  seatHold;

    }

    @Override
    public String reserveSeats(UUID seatHoldId, String customerEmail) {
        Optional<SeatHold> seatHold = seatHoldCache.retrieveSeatHoldById(seatHoldId, customerEmail);

        if(seatHold.isPresent()){
            if(seatHold.get().isHoldExpired()){
                throw new TicketServiceException("Seats have been released. Hold has expired.");
            }else{
                seatHold.get().reserveHeldSeats();
                seatRepository.saveAll(seatHold.get().getHeldSeats());
                return RESERVATION_SUCCESS_RESPONSE_CODE;
            }
        }else{
            throw new TicketServiceException("No seats held for the customer or seat hold id.");
        }
    }
}