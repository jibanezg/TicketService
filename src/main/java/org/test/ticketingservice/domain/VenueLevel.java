package org.test.ticketingservice.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.test.ticketingservice.constants.SeatStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * VenueLevel class represents a section of a venue and its relevant associated data.
 * As per the requirements at the moment, this class should be final (Not intended to be
 * extended as it is not necessary). However, for the sake of simplicity, it was made
 * non-final in order to use JPA.
 *
 * For simplicity, it was assumed that, when a new venue is created, all of its seats
 * are automatically setup by using the number of rows and seats per row in the venue
 * level.
 */
@Entity
@Table(name = "venue_levels")
public class VenueLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private int levelId;

    @Column(name = "level_name")
    private String levelName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "seats_per_row")
    private Integer seatsPerRow;

    @Column(name = "rows")
    private Integer rows;

    @OneToMany(mappedBy = "venueLevel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Seat> seats;

    public VenueLevel(int levelId, String levelName, BigDecimal price, int rows, int seatsPerRow) {
        this.levelId = levelId;
        this.levelName = levelName;
        this.price = price;
        this.seatsPerRow = seatsPerRow;
        this.rows = rows;
        this.seats = new ArrayList<>();

        initializeVenue();
    }

    private void initializeVenue(){
        for (int row = 1; row <= rows; row++) {
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                seats.add(new Seat(row, seatNum, this));
            }
        }
    }

    public VenueLevel(){}

    public List<Seat> getAvailableSeats() {
        return seats.stream()
                .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
    }
}
