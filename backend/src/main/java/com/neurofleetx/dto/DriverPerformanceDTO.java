package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverPerformanceDTO {
    private Long driverId;
    private String driverName;
    private String email;
    private Integer totalTrips;
    private Double averageRating;
    private Double totalRevenue;
    private Integer onTimePercentage;
    private Integer totalIssues;
    private Integer completedTrips;
    private Integer cancelledTrips;
    private Double averageSpeed;
    private Double totalDistance;
    private Integer totalHours;
    private String status; // ACTIVE, INACTIVE, ON_LEAVE
}
