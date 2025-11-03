package com.neurofleetx.dto;

import com.neurofleetx.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteOptimizationResponse {
    private Route primaryRoute;
    private List<Route> alternativeRoutes;
    private String optimizationAlgorithm;
    private Integer totalRoutesAnalyzed;
    private Double timeSavedMinutes;
    private Double energySavedPercent;
}
