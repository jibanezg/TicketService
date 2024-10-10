package org.test.ticketingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.test.ticketingservice.domain.VenueLevel;

import java.util.List;

/**
 * Repository interface for the aggregate root VenueLevel
 */
public interface VenueLevelRepository extends JpaRepository<VenueLevel,Integer> {

    @Query("SELECT DISTINCT vl FROM VenueLevel vl " +
            "JOIN FETCH vl.seats s " +
            "WHERE vl.levelId BETWEEN :minLevel AND :maxLevel " +
            "ORDER BY vl.levelId ASC")
    List<VenueLevel> findAvailableSeatsInVenueLevels(@Param("minLevel") int minLevel, @Param("maxLevel") int maxLevel);

}
