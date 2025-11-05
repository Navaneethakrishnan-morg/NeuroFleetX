package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapDataPoint {
    private Double latitude;
    private Double longitude;
    private Integer intensity;
    private String type;
    private String vehicleId;
    private String location;
}
