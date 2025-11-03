package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteOptimizationRequest {
    private Long vehicleId;
    private String startLocation;
    private String endLocation;
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    private Boolean includeTrafficData;
    private Boolean generateAlternatives;
}
