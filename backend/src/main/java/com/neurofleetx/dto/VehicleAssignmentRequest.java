package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAssignmentRequest {
    private Long loadId;
    private String destination;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private Double weight;
    private String priority;
}
