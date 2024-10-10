package org.test.ticketingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.test.ticketingservice.constants.SeatStatus;
import org.test.ticketingservice.config.ApplicationProperties;
import org.test.ticketingservice.domain.SeatHold;
import org.test.ticketingservice.cache.SeatHoldCache;
import org.test.ticketingservice.domain.VenueLevel;
import org.test.ticketingservice.exception.TicketServiceException;
import org.test.ticketingservice.repository.SeatRepository;
import org.test.ticketingservice.repository.VenueLevelRepository;
import org.test.ticketingservice.service.impl.TicketServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {TicketServiceImpl.class})
@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Autowired
    private TicketServiceImpl ticketService;

    @MockBean
    private VenueLevelRepository venueLevelRepository;

    @MockBean
    private SeatRepository seatRepository;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private SeatHoldCache seatHoldCache;

    @MockBean
    private VenueLevel venueLevel;

    @Test
    public void testNumAvailableSeats_Success(){

        VenueLevel mockedVenueLevel = mockVenueLevel(1,"Orchestra", BigDecimal.valueOf(100.00), 50, 25);

        when(venueLevelRepository.findById(anyInt())).thenReturn(Optional.of(mockedVenueLevel));
        when(venueLevel.getAvailableSeats()).thenReturn(mockedVenueLevel.getAvailableSeats());

        int result = ticketService.numSeatsAvailable(Optional.of(1));

        assertEquals(mockedVenueLevel.getAvailableSeats().size(), result);
    }

    @Test
    public void testNumAvailableSeats_Failure_WhenEmptyLevelId(){

        assertThrows(TicketServiceException.class, () -> {
            ticketService.numSeatsAvailable(Optional.empty());
        });
    }

    @Test
    public void testNumAvailableSeats_Success_AllHeld(){
        VenueLevel mockedVenueLevel = mockVenueLevelWithHeldSeats();

        when(venueLevelRepository.findById(anyInt())).thenReturn(Optional.of(mockedVenueLevel));
        when(venueLevel.getAvailableSeats()).thenReturn(mockedVenueLevel.getAvailableSeats());

        int result = ticketService.numSeatsAvailable(Optional.of(1));

        assertEquals(0, result);
    }

    @Test
    public void testFindAndHoldSeats_Success_SeatHoldNotNull(){

        when(applicationProperties.getVenueMinLevel()).thenReturn("1");
        when(applicationProperties.getVenueMaxLevel()).thenReturn("4");
        when(applicationProperties.getHoldDurationSeconds()).thenReturn("60");
        when(venueLevelRepository.findAvailableSeatsInVenueLevels(anyInt(), anyInt())).thenReturn(mockVenueLevels());

        SeatHold result = ticketService.findAndHoldSeats(5, Optional.of(1),Optional.of(4), "test@test.com");

        assertNotNull(result);
    }

    @Test
    public void testFindAndHoldSeats_Success_WhenAvailableSeats(){

        when(applicationProperties.getVenueMinLevel()).thenReturn("1");
        when(applicationProperties.getVenueMaxLevel()).thenReturn("4");
        when(applicationProperties.getHoldDurationSeconds()).thenReturn("60");
        when(venueLevelRepository.findAvailableSeatsInVenueLevels(anyInt(), anyInt())).thenReturn(mockVenueLevels());

        SeatHold result = ticketService.findAndHoldSeats(5, Optional.of(1),Optional.of(4), "test@test.com");

        assertEquals(5, result.getHeldSeats().size());

        verify(venueLevelRepository).findAvailableSeatsInVenueLevels(1, 4);
    }


    @Test
    public void testFindAndHoldSeats_Failure_WhenNumSeatsGreaterThanAvailableSeats(){

        when(applicationProperties.getVenueMinLevel()).thenReturn("1");
        when(applicationProperties.getVenueMaxLevel()).thenReturn("4");
        when(applicationProperties.getHoldDurationSeconds()).thenReturn("60");
        when(venueLevelRepository.findAvailableSeatsInVenueLevels(anyInt(), anyInt())).thenReturn(mockVenueLevels());

        assertThrows(TicketServiceException.class, () -> {
            ticketService.findAndHoldSeats(200000, Optional.of(1),Optional.of(2), "test@test.com");
        });
    }

    @Test
    public void testFindAndHoldSeats_Failure_WhenNumSeatsIsZero(){

        when(applicationProperties.getVenueMinLevel()).thenReturn("1");
        when(applicationProperties.getVenueMaxLevel()).thenReturn("4");
        when(applicationProperties.getHoldDurationSeconds()).thenReturn("60");
        when(venueLevelRepository.findAvailableSeatsInVenueLevels(anyInt(), anyInt())).thenReturn(mockVenueLevels());

        assertThrows(TicketServiceException.class, () -> {
            ticketService.findAndHoldSeats(0, Optional.of(1),Optional.of(2), "test@test.com");
        });
    }




    private VenueLevel mockVenueLevel(Integer levelId, String name, BigDecimal price, Integer rows, Integer seatsPerRow){
        return new VenueLevel(1,"Orchestra", BigDecimal.valueOf(100.00), 50, 25);
    }

    private VenueLevel mockVenueLevelWithHeldSeats(){
        VenueLevel venueLevelMock = mockVenueLevel(1, "Orchestra", BigDecimal.valueOf(100.00), 50, 25);
        venueLevelMock.getAvailableSeats().forEach(as ->{
            as.setStatus(SeatStatus.HELD);
        });
        return venueLevelMock;
    }

    private List<VenueLevel> mockVenueLevels(){
        List<VenueLevel> venueLevels = new ArrayList<>();

        venueLevels.add(mockVenueLevel(1,"Orchestra", BigDecimal.valueOf(100.00), 50, 25));
        venueLevels.add(mockVenueLevel(2,"Main",BigDecimal.valueOf(75.00), 20, 100));
        venueLevels.add(mockVenueLevel(3,"Balcony 1'", BigDecimal.valueOf(50.00), 15, 100));
        venueLevels.add(mockVenueLevel(4,"Balcony 2'", BigDecimal.valueOf(40.00), 15, 100));

        return venueLevels;

    }
}
