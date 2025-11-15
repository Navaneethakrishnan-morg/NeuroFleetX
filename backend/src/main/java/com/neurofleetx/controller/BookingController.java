package com.neurofleetx.controller;

import com.neurofleetx.dto.BookingAvailabilityRequest;
import com.neurofleetx.dto.BookingAvailabilityResponse;
import com.neurofleetx.dto.VehicleRecommendationResponse;
import com.neurofleetx.dto.VehicleSearchRequest;
import com.neurofleetx.model.Booking;
import com.neurofleetx.service.BookingService;
import com.neurofleetx.service.RecommendationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private RecommendationEngine recommendationEngine;

    @GetMapping("/admin/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/customer/bookings")
    public ResponseEntity<List<Booking>> getCustomerBookings(@RequestParam String username) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(username));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping("/customer/bookings")
    public ResponseEntity<Booking> createBooking(@RequestParam String username, @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(username, booking));
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.updateBooking(id, booking));
    }

    @GetMapping("/customer/bookings/recommended")
    public ResponseEntity<List<Booking>> getRecommendedBookings(@RequestParam String username) {
        return ResponseEntity.ok(bookingService.getRecommendedBookings(username));
    }

    @PostMapping("/customer/bookings/search")
    public ResponseEntity<List<VehicleRecommendationResponse>> searchVehicles(
            @RequestParam String username,
            @RequestBody VehicleSearchRequest searchRequest) {
        return ResponseEntity.ok(recommendationEngine.searchVehicles(username, searchRequest));
    }

    @PostMapping("/customer/bookings/availability")
    public ResponseEntity<List<BookingAvailabilityResponse>> checkAvailability(
            @RequestBody BookingAvailabilityRequest request) {
        return ResponseEntity.ok(recommendationEngine.checkVehicleAvailability(request));
    }

    @PutMapping("/customer/bookings/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    // Manager endpoints
    @GetMapping("/manager/bookings/pending")
    public ResponseEntity<List<Booking>> getPendingBookingsForManager() {
        return ResponseEntity.ok(bookingService.getPendingBookingsForManager());
    }

    @PutMapping("/manager/bookings/{id}/approve")
    public ResponseEntity<Booking> approveBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.approveBookingByManager(id));
    }

    @PutMapping("/manager/bookings/{id}/assign-driver")
    public ResponseEntity<Booking> assignDriver(@PathVariable Long id, @RequestParam Long driverId) {
        return ResponseEntity.ok(bookingService.assignDriverToBooking(id, driverId));
    }

    @GetMapping("/manager/drivers/available")
    public ResponseEntity<List<com.neurofleetx.model.User>> getAvailableDrivers() {
        return ResponseEntity.ok(bookingService.getAvailableDrivers());
    }

    // Driver endpoints
    @GetMapping("/driver/bookings")
    public ResponseEntity<List<Booking>> getDriverBookings(@RequestParam String username) {
        return ResponseEntity.ok(bookingService.getDriverAssignedBookings(username));
    }

    @PutMapping("/driver/bookings/{id}/start-trip")
    public ResponseEntity<Booking> startTrip(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.startTripByDriver(id));
    }
}
