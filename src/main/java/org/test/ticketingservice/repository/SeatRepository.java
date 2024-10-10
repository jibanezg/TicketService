package org.test.ticketingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.ticketingservice.domain.Seat;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
}
