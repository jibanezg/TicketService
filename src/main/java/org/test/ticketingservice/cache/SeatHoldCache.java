package org.test.ticketingservice.cache;

import org.springframework.stereotype.Component;
import org.test.ticketingservice.domain.SeatHold;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Seat hold cache class that works as a mechanism to hold seats in a per-user basis.
 * This class offers two main methods:
 * <p>
 * {@link this#cacheSeatHold(SeatHold)}: Stores a list of seats that will be temporarily
 * held under the user's email
 * {@link this#retrieveSeatHoldById(UUID, String)} which is used so that the client can
 * reserve seats at a later time in a potential multistep process.
 * <p>
 * Another reason to follow this approach is because SeatHold instances are shor-lived.
 * The overhead associated with storing, retrieving, and managing them in a long-term
 * persistence layer is not justified.
 * <p>
 * Removal of expired instances is left for future improvement (Probably using a
 * scheduled job) :)
 */
@Component
public class SeatHoldCache {

    private Map<SeatHoldCacheKey, SeatHold> seatHoldMap;

    public SeatHoldCache(){
        this.seatHoldMap = new HashMap<>();
    }

    public Optional<SeatHold> retrieveSeatHoldById(UUID seatHoldId, String customerEmail){

        SeatHoldCacheKey key = new SeatHoldCacheKey(seatHoldId, customerEmail);
        Optional<SeatHold> seatHoldOptional = Optional.of(seatHoldMap.get(key));
        if(seatHoldOptional.isPresent() && !seatHoldOptional.get().isHoldExpired()){
            return seatHoldOptional;
        }else{
            this.seatHoldMap.remove(key);
            return Optional.empty();
        }
    }

    public void cacheSeatHold(SeatHold seatHold){
        this.seatHoldMap.putIfAbsent(new SeatHoldCacheKey(seatHold.getSeatHoldId(), seatHold.getCustomerEmail()), seatHold);
    }
}
