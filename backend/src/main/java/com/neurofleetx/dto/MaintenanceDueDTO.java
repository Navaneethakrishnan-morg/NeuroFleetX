package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDueDTO {
    private Long vehicleId;
    private String vehicleNumber;
    private String manufacturer;
    private String model;
    private String maintenanceType; // OIL_CHANGE, TIRE_ROTATION, INSPECTION, REPAIR
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private LocalDateTime dueDate;
    private Integer mileage;
    private Integer daysSinceLast;
    private Integer fuelLevel;
    private Integer batteryLevel;
    private String status; // DUE, OVERDUE, UPCOMING
    private String additionalNotes;
}
