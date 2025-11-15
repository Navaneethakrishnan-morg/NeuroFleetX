package com.neurofleetx.service;

import com.neurofleetx.model.Booking;
import com.neurofleetx.model.User;
import com.neurofleetx.model.Vehicle;
import com.neurofleetx.repository.BookingRepository;
import com.neurofleetx.repository.UserRepository;
import com.neurofleetx.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private static final double HOURLY_RATE = 25.0;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> getBookingsByCustomer(String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    public List<Booking> getBookingsByVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return bookingRepository.findByVehicle(vehicle);
    }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Vehicle> getAvailableVehicles(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> overlappingBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED 
                        && b.getStatus() != Booking.BookingStatus.COMPLETED)
                .filter(b -> !(b.getEndTime().isBefore(startTime) || b.getStartTime().isAfter(endTime)))
                .collect(Collectors.toList());

        List<Long> bookedVehicleIds = overlappingBookings.stream()
                .map(b -> b.getVehicle().getId())
                .collect(Collectors.toList());

        return vehicleRepository.findAll().stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.AVAILABLE)
                .filter(v -> !bookedVehicleIds.contains(v.getId()))
                .collect(Collectors.toList());
    }

    public boolean isVehicleAvailable(Long vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (vehicle.getStatus() != Vehicle.VehicleStatus.AVAILABLE) {
            return false;
        }

        List<Booking> vehicleBookings = bookingRepository.findByVehicle(vehicle).stream()
                .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED 
                        && b.getStatus() != Booking.BookingStatus.COMPLETED)
                .collect(Collectors.toList());

        return vehicleBookings.stream()
                .noneMatch(b -> !(b.getEndTime().isBefore(startTime) || b.getStartTime().isAfter(endTime)));
    }

    public Booking createBooking(String username, Booking booking) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Vehicle vehicle = vehicleRepository.findById(booking.getVehicle().getId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!isVehicleAvailable(vehicle.getId(), booking.getStartTime(), booking.getEndTime())) {
            throw new RuntimeException("Vehicle is not available for the selected time period");
        }

        booking.setCustomer(customer);
        booking.setVehicle(vehicle);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setTotalPrice(calculateTotalPrice(booking.getStartTime(), booking.getEndTime()));
        return bookingRepository.save(booking);
    }

    public List<Booking> getPendingBookingsForManager() {
        return bookingRepository.findByStatus(Booking.BookingStatus.PENDING);
    }

    public Booking approveBookingByManager(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending approval status");
        }
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking assignDriverToBooking(Long bookingId, Long driverId) {
        Booking booking = getBookingById(bookingId);
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        if (driver.getRole() != User.UserRole.DRIVER) {
            throw new RuntimeException("Selected user is not a driver");
        }
        
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking must be confirmed before assigning driver");
        }
        
        booking.setDriver(driver);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public List<User> getAvailableDrivers() {
        return userRepository.findByRole(User.UserRole.DRIVER).stream()
                .filter(User::getActive)
                .collect(Collectors.toList());
    }

    public List<Booking> getDriverAssignedBookings(String driverUsername) {
        User driver = userRepository.findByUsername(driverUsername)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return bookingRepository.findByDriver(driver);
    }

    public Booking startTripByDriver(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking must be confirmed to start trip");
        }
        booking.setStatus(Booking.BookingStatus.IN_PROGRESS);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = getBookingById(id);
        
        if (bookingDetails.getVehicle() != null && !booking.getVehicle().getId().equals(bookingDetails.getVehicle().getId())) {
            if (!isVehicleAvailable(bookingDetails.getVehicle().getId(), bookingDetails.getStartTime(), bookingDetails.getEndTime())) {
                throw new RuntimeException("Vehicle is not available for the selected time period");
            }
            booking.setVehicle(bookingDetails.getVehicle());
        }
        
        booking.setStatus(bookingDetails.getStatus());
        booking.setStartTime(bookingDetails.getStartTime());
        booking.setEndTime(bookingDetails.getEndTime());
        booking.setPickupLocation(bookingDetails.getPickupLocation());
        booking.setDropoffLocation(bookingDetails.getDropoffLocation());
        booking.setTotalPrice(calculateTotalPrice(bookingDetails.getStartTime(), bookingDetails.getEndTime()));
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking startBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(Booking.BookingStatus.IN_PROGRESS);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking completeBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public List<Booking> getRecommendedBookings(String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Booking> bookings = bookingRepository.findByCustomerOrderByCreatedAtDesc(customer);
        bookings.forEach(b -> b.setIsRecommended(true));
        return bookings;
    }

    private double calculateTotalPrice(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        double hours = duration.toHours() + (duration.toMinutesPart() / 60.0);
        return Math.round(hours * HOURLY_RATE * 100.0) / 100.0;
    }
}
