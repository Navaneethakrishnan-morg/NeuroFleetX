package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAvailabilityResponse {
    private Long vehicleId;
    private List<TimeSlot> availableSlots;
    private List<TimeSlot> bookedSlots;
    private Double pricePerHour;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlot {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double price;
        private Boolean isAvailable;
    }
}
