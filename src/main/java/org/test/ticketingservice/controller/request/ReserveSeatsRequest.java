package org.test.ticketingservice.controller.request;

import java.util.UUID;

public class ReserveSeatsRequest {
    private UUID seatHoldId;
    private String customerEmail;

    public UUID getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(UUID seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
