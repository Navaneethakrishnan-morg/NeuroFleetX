package com.neurofleetx.service;

import com.neurofleetx.dto.BookingAvailabilityRequest;
import com.neurofleetx.dto.BookingAvailabilityResponse;
import com.neurofleetx.dto.VehicleRecommendationResponse;
import com.neurofleetx.dto.VehicleSearchRequest;
import com.neurofleetx.model.*;
import com.neurofleetx.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationEngine {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerPreferenceRepository preferenceRepository;

    @Autowired
    private VehicleRatingRepository ratingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    private static final double HOURLY_RATE_BASE = 25.0;
    private static final Map<Vehicle.VehicleType, Double> TYPE_MULTIPLIERS = Map.of(
        Vehicle.VehicleType.SEDAN, 1.0,
        Vehicle.VehicleType.SUV, 1.5,
        Vehicle.VehicleType.VAN, 2.0,
        Vehicle.VehicleType.TRUCK, 2.5,
        Vehicle.VehicleType.BUS, 3.0,
        Vehicle.VehicleType.BIKE, 0.5
    );

    public List<VehicleRecommendationResponse> getRecommendedVehicles(String username, VehicleSearchRequest searchRequest) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Vehicle> availableVehicles = getAvailableVehiclesForSearch(searchRequest);
        
        Optional<CustomerPreference> preferenceOpt = preferenceRepository.findByCustomer(customer);
        
        return availableVehicles.stream()
                .map(vehicle -> buildRecommendation(vehicle, preferenceOpt, searchRequest))
                .sorted(Comparator.comparing(VehicleRecommendationResponse::getRecommendationScore).reversed())
                .collect(Collectors.toList());
    }

    private List<Vehicle> getAvailableVehiclesForSearch(VehicleSearchRequest searchRequest) {
        List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.AVAILABLE)
                .collect(Collectors.toList());

        if (searchRequest.getVehicleType() != null && !searchRequest.getVehicleType().equals("ALL")) {
            vehicles = vehicles.stream()
                    .filter(v -> v.getType().name().equals(searchRequest.getVehicleType()))
                    .collect(Collectors.toList());
        }

        if (searchRequest.getIsElectric() != null) {
            vehicles = vehicles.stream()
                    .filter(v -> v.getIsElectric().equals(searchRequest.getIsElectric()))
                    .collect(Collectors.toList());
        }

        if (searchRequest.getMinCapacity() != null) {
            vehicles = vehicles.stream()
                    .filter(v -> v.getCapacity() >= searchRequest.getMinCapacity())
                    .collect(Collectors.toList());
        }

        if (searchRequest.getMaxCapacity() != null) {
            vehicles = vehicles.stream()
                    .filter(v -> v.getCapacity() <= searchRequest.getMaxCapacity())
                    .collect(Collectors.toList());
        }

        if (searchRequest.getStartTime() != null && searchRequest.getEndTime() != null) {
            vehicles = filterByAvailability(vehicles, searchRequest.getStartTime(), searchRequest.getEndTime());
        }

        return vehicles;
    }

    private List<Vehicle> filterByAvailability(List<Vehicle> vehicles, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> overlappingBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED 
                        && b.getStatus() != Booking.BookingStatus.COMPLETED)
                .filter(b -> !(b.getEndTime().isBefore(startTime) || b.getStartTime().isAfter(endTime)))
                .collect(Collectors.toList());

        Set<Long> bookedVehicleIds = overlappingBookings.stream()
                .map(b -> b.getVehicle().getId())
                .collect(Collectors.toSet());

        return vehicles.stream()
                .filter(v -> !bookedVehicleIds.contains(v.getId()))
                .collect(Collectors.toList());
    }

    private VehicleRecommendationResponse buildRecommendation(Vehicle vehicle, Optional<CustomerPreference> preferenceOpt, VehicleSearchRequest searchRequest) {
        double score = calculateRecommendationScore(vehicle, preferenceOpt);
        String reason = generateRecommendationReason(vehicle, preferenceOpt);
        boolean isRecommended = score >= 70.0;
        double pricePerHour = calculatePricePerHour(vehicle);

        return new VehicleRecommendationResponse(
            vehicle,
            score,
            reason,
            isRecommended,
            pricePerHour,
            true
        );
    }

    private double calculateRecommendationScore(Vehicle vehicle, Optional<CustomerPreference> preferenceOpt) {
        double score = 50.0;

        if (preferenceOpt.isPresent()) {
            CustomerPreference pref = preferenceOpt.get();

            if (pref.getPreferredVehicleType() != null && pref.getPreferredVehicleType() == vehicle.getType()) {
                score += 20.0;
            }

            if (pref.getPreferredElectric() != null && pref.getPreferredElectric().equals(vehicle.getIsElectric())) {
                score += 15.0;
            }

            if (pref.getPreferredCapacity() != null && Math.abs(pref.getPreferredCapacity() - vehicle.getCapacity()) <= 2) {
                score += 10.0;
            }
        }

        Double avgRating = ratingRepository.findAverageRatingByVehicle(vehicle);
        if (avgRating != null) {
            score += (avgRating / 5.0) * 15.0;
        }

        if (vehicle.getHealthScore() != null) {
            score += (vehicle.getHealthScore() / 100.0) * 10.0;
        }

        if (vehicle.getIsElectric()) {
            score += 5.0;
        }

        return Math.min(Math.round(score * 10.0) / 10.0, 100.0);
    }

    private String generateRecommendationReason(Vehicle vehicle, Optional<CustomerPreference> preferenceOpt) {
        List<String> reasons = new ArrayList<>();

        if (preferenceOpt.isPresent()) {
            CustomerPreference pref = preferenceOpt.get();

            if (pref.getPreferredVehicleType() != null && pref.getPreferredVehicleType() == vehicle.getType()) {
                reasons.add("Matches your preferred vehicle type");
            }

            if (pref.getPreferredElectric() != null && pref.getPreferredElectric().equals(vehicle.getIsElectric())) {
                reasons.add("Matches your eco-friendly preference");
            }

            if (pref.getBookingCount() != null && pref.getBookingCount() > 0) {
                reasons.add("Based on your " + pref.getBookingCount() + " previous bookings");
            }
        }

        Double avgRating = ratingRepository.findAverageRatingByVehicle(vehicle);
        if (avgRating != null && avgRating >= 4.0) {
            reasons.add(String.format("Highly rated (%.1f/5.0)", avgRating));
        }

        if (vehicle.getHealthScore() != null && vehicle.getHealthScore() >= 90) {
            reasons.add("Excellent condition");
        }

        if (vehicle.getIsElectric()) {
            reasons.add("Eco-friendly electric vehicle");
        }

        return reasons.isEmpty() ? "Available and reliable" : String.join(" • ", reasons);
    }

    private double calculatePricePerHour(Vehicle vehicle) {
        double multiplier = TYPE_MULTIPLIERS.getOrDefault(vehicle.getType(), 1.0);
        double price = HOURLY_RATE_BASE * multiplier;
        
        if (vehicle.getIsElectric()) {
            price *= 1.1;
        }
        
        return Math.round(price * 100.0) / 100.0;
    }

    public void updateCustomerPreferences(User customer) {
        List<Booking> completedBookings = bookingRepository.findByCustomer(customer).stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                .collect(Collectors.toList());

        if (completedBookings.isEmpty()) {
            return;
        }

        Map<Vehicle.VehicleType, Long> typeFrequency = completedBookings.stream()
                .collect(Collectors.groupingBy(
                    b -> b.getVehicle().getType(),
                    Collectors.counting()
                ));

        Vehicle.VehicleType mostFrequentType = typeFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        long electricCount = completedBookings.stream()
                .filter(b -> b.getVehicle().getIsElectric())
                .count();
        boolean preferElectric = electricCount > (completedBookings.size() / 2.0);

        double avgCapacity = completedBookings.stream()
                .mapToInt(b -> b.getVehicle().getCapacity())
                .average()
                .orElse(5.0);

        double avgDuration = completedBookings.stream()
                .mapToLong(b -> Duration.between(b.getStartTime(), b.getEndTime()).toHours())
                .average()
                .orElse(24.0);

        CustomerPreference preference = preferenceRepository.findByCustomer(customer)
                .orElse(new CustomerPreference());

        preference.setCustomer(customer);
        preference.setPreferredVehicleType(mostFrequentType);
        preference.setPreferredElectric(preferElectric);
        preference.setPreferredCapacity((int) Math.round(avgCapacity));
        preference.setAverageBookingDuration((int) Math.round(avgDuration));
        preference.setBookingCount(completedBookings.size());
        preference.setUpdatedAt(LocalDateTime.now());

        preferenceRepository.save(preference);
    }

    public List<VehicleRecommendationResponse> searchVehicles(String username, VehicleSearchRequest searchRequest) {
        return getRecommendedVehicles(username, searchRequest);
    }

    public List<BookingAvailabilityResponse> checkVehicleAvailability(BookingAvailabilityRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        double pricePerHour = calculatePricePerHour(vehicle);

        List<Booking> vehicleBookings = bookingRepository.findByVehicle(vehicle).stream()
                .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED 
                        && b.getStatus() != Booking.BookingStatus.COMPLETED)
                .collect(Collectors.toList());

        List<BookingAvailabilityResponse.TimeSlot> availableSlots = new ArrayList<>();
        List<BookingAvailabilityResponse.TimeSlot> bookedSlots = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (int hour = 0; hour < 24; hour++) {
                LocalDateTime slotStart = LocalDateTime.of(date, LocalTime.of(hour, 0));
                LocalDateTime slotEnd = slotStart.plusHours(1);

                boolean isAvailable = vehicleBookings.stream()
                        .noneMatch(b -> !(b.getEndTime().isBefore(slotStart) || b.getStartTime().isAfter(slotEnd)));

                BookingAvailabilityResponse.TimeSlot slot = new BookingAvailabilityResponse.TimeSlot(
                    slotStart,
                    slotEnd,
                    pricePerHour,
                    isAvailable
                );

                if (isAvailable) {
                    availableSlots.add(slot);
                } else {
                    bookedSlots.add(slot);
                }
            }
        }

        BookingAvailabilityResponse response = new BookingAvailabilityResponse(
            vehicle.getId(),
            availableSlots,
            bookedSlots,
            pricePerHour
        );

        return List.of(response);
    }
}
