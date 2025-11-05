package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchRequest {
    private String vehicleType;
    private Boolean isElectric;
    private Integer minCapacity;
    private Integer maxCapacity;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String sortBy;
}
