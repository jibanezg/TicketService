package org.test.ticketingservice.cache;

import org.test.ticketingservice.controller.request.HoldSeatsRequest;

import java.util.Objects;
import java.util.UUID;

/**
 * Seat hold cache key class implemented to allow multiple potential holds for scalability purposes.
 * A seat hold cache key is a combination of the seat hold id (UUID generated) and a customer email.
 * It is assumed that the client knows the generated UUID in a {@link org.test.ticketingservice.domain.SeatHold}
 * when the endpoint {@link org.test.ticketingservice.controller.TicketController#findAndHoldSeats} is consumed.
 */
public class SeatHoldCacheKey {

    private UUID uuid;
    private String customerEmail;

    public SeatHoldCacheKey(UUID uuid, String customerEmail) {
        this.uuid = uuid;
        this.customerEmail = customerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatHoldCacheKey that = (SeatHoldCacheKey) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(customerEmail, that.customerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, customerEmail);
    }
}
