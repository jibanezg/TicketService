package org.test.ticketingservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.test.ticketingservice.constants.SeatStatus;

/**
 * A venue seat. Same as with VenueLevel this class would normally be final
 * as per the current version of the requirements. This class is now extendable
 * for JPA purposes.
 */
@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int rowNumber;
    private int number;
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "level_id")
    @JsonBackReference
    private VenueLevel venueLevel;

    public Seat(int rowNumber, int number, VenueLevel venueLevel){
        this.rowNumber = rowNumber;
        this.number = number;
        this.status = SeatStatus.AVAILABLE;
        this.venueLevel = venueLevel;
    }

    public Seat(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public VenueLevel getVenueLevel() {
        return venueLevel;
    }

    public void setVenueLevel(VenueLevel venueLevel) {
        this.venueLevel = venueLevel;
    }
}
