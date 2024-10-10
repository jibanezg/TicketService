package org.test.ticketingservice.controller;


import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.ticketingservice.constants.HttpStatusCodes;
import org.test.ticketingservice.controller.request.HoldSeatsRequest;
import org.test.ticketingservice.controller.request.ReserveSeatsRequest;
import org.test.ticketingservice.controller.response.ApiReturn;
import org.test.ticketingservice.controller.validation.VenueLevelValidator;
import org.test.ticketingservice.domain.SeatHold;
import org.test.ticketingservice.service.TicketService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
public class TicketController {

    private final TicketService ticketService;
    private final VenueLevelValidator venueLevelValidator;

    public TicketController(TicketService ticketService, VenueLevelValidator venueLevelValidator){
        this.ticketService = ticketService;
        this.venueLevelValidator = venueLevelValidator;
    }

    @Operation(summary = "Returns the number of seats in the requested level that are neither held nor reserved.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Successful Request"),
            @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Bad request")
    })
    @GetMapping("/venue-levels/{levelId}/seats/available/count")
    public ResponseEntity<ApiReturn<Integer>> numSeatsAvailable(@PathVariable("levelId") Integer levelId){

        if(!venueLevelValidator.isValidVenueLevel(levelId)){
            ApiReturn<Integer> response = new ApiReturn<>("Request Failed", List.of("The venue level is invalid."));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Integer numSeatsAvailable = ticketService.numSeatsAvailable(Optional.of(levelId));
        ApiReturn<Integer> response = new ApiReturn<>("Request successful", numSeatsAvailable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Finds and holds the best available seats for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Successful Request"),
            @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Bad request")
    })
    @PutMapping("/venue-levels/seats/hold")
    public ResponseEntity<ApiReturn<SeatHold>> findAndHoldSeats(@RequestBody HoldSeatsRequest holdSeatsRequest){

        Optional<Integer> minValueLevel = Optional.ofNullable(holdSeatsRequest.getMinLevel());
        Optional<Integer> maxValueLevel = Optional.ofNullable(holdSeatsRequest.getMaxLevel());

        if(!venueLevelValidator.isValidVenueLevelRange(minValueLevel, maxValueLevel)){
            ApiReturn<SeatHold> response = new ApiReturn<>("Request Failed", List.of("Venue level range is invalid"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        SeatHold seatHold = ticketService.findAndHoldSeats(holdSeatsRequest.getNumSeats(), minValueLevel, maxValueLevel, holdSeatsRequest.getCustomerEmail());
        ApiReturn<SeatHold> response = new ApiReturn<>("Request successful.", seatHold);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Commits seats held for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Successful Request"),
            @ApiResponse(responseCode = HttpStatusCodes.BAD_REQUEST, description = "Bad request")
    })
    @PutMapping("/venue-levels/seats/reserve")
    public ResponseEntity<ApiReturn<String>> reserveSeats(@RequestBody ReserveSeatsRequest reserveSeatsRequest){

        if(reserveSeatsRequest.getSeatHoldId() == null || StringUtils.isEmpty(reserveSeatsRequest.getCustomerEmail())){
            ApiReturn<String> response = new ApiReturn<>("Request Failed", List.of("Seat hold id and customer email are mandatory"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String result = ticketService.reserveSeats(reserveSeatsRequest.getSeatHoldId(), reserveSeatsRequest.getCustomerEmail());
        ApiReturn<String> response = new ApiReturn<>("Request successful.", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
