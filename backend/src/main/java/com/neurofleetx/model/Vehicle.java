package com.neurofleetx.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String manufacturer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @Column(nullable = false)
    private Integer capacity;

    private Boolean isElectric = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    private Double latitude;

    private Double longitude;

    private Integer batteryLevel;

    private Integer fuelLevel;

    private Integer mileage = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LocalDateTime lastMaintenanceDate;

    private Integer healthScore = 100;

    public enum VehicleType {
        SEDAN, SUV, VAN, TRUCK, BUS, BIKE
    }

    public enum VehicleStatus {
        AVAILABLE, IN_USE, MAINTENANCE, OUT_OF_SERVICE
    }
}
