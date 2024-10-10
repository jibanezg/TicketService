package org.test.ticketingservice.domain;

import org.test.ticketingservice.constants.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class SeatHold {

    private final UUID seatHoldId;
    private String customerEmail;
    private List<Seat> heldSeats;
    private final LocalDateTime holdExpiryTime;


    public SeatHold(String customerEmail, List<Seat> heldSeats, int holdDurationSeconds){
        this.seatHoldId = UUID.randomUUID();
        this.customerEmail = customerEmail;
        this.heldSeats = heldSeats;
        holdExpiryTime = LocalDateTime.now().plusSeconds(holdDurationSeconds);

        holdSeats();
    }


    public List<Seat> getHeldSeats() {
        return heldSeats;
    }


    private void holdSeats(){
        this.heldSeats.forEach(hs -> hs.setStatus(SeatStatus.HELD));
    }

    public boolean isHoldExpired() {
        return LocalDateTime.now().isAfter(holdExpiryTime);
    }

    public void reserveHeldSeats(){
        this.heldSeats.forEach(hs -> hs.setStatus(SeatStatus.RESERVED));
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public UUID getSeatHoldId() {
        return seatHoldId;
    }
}
