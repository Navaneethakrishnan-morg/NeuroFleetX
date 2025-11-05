package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyActivityData {
    private List<Integer> hours;
    private List<Integer> bookingCounts;
    private List<Double> revenues;
    private List<Integer> vehicleUsage;
}
