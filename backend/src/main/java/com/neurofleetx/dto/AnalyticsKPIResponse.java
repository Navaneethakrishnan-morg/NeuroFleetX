package com.neurofleetx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsKPIResponse {
    private Integer totalFleet;
    private Integer availableVehicles;
    private Integer inUseVehicles;
    private Integer maintenanceVehicles;
    private Integer tripsToday;
    private Integer activeRoutes;
    private Double totalRevenue;
    private Double revenueToday;
    private Double averageUtilization;
    private Integer pendingMaintenance;
    private Integer totalCustomers;
    private Integer activeCustomers;
}
