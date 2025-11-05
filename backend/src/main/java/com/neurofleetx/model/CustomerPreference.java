package com.neurofleetx.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private User customer;

    @Enumerated(EnumType.STRING)
    private Vehicle.VehicleType preferredVehicleType;

    private Boolean preferredElectric = false;

    private Integer preferredCapacity;

    private Integer averageBookingDuration;

    @Column(columnDefinition = "TEXT")
    private String preferredLocations;

    private Integer bookingCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
