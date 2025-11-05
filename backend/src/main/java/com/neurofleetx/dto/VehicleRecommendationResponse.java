package com.neurofleetx.dto;

import com.neurofleetx.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRecommendationResponse {
    private Vehicle vehicle;
    private Double recommendationScore;
    private String reason;
    private Boolean isRecommended;
    private Double pricePerHour;
    private Boolean isAvailable;
}
