package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetDistributionData {
    private List<HeatmapDataPoint> vehicleLocations;
    private List<HeatmapDataPoint> tripDensity;
    private Map<String, Integer> typeDistribution;
    private Map<String, Integer> statusDistribution;
}
