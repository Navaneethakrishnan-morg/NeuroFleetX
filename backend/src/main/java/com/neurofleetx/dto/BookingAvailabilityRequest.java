package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAvailabilityRequest {
    private Long vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
}
