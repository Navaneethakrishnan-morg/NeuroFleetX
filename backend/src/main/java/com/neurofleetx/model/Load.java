package com.neurofleetx.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "loads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Load {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long loadId;

    private Long vehicleId;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private String destination;

    private Double destinationLatitude;
    private Double destinationLongitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    private Long assignedRouteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadStatus status = LoadStatus.PENDING;

    private String pickupLocation;
    private Double pickupLatitude;
    private Double pickupLongitude;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime assignedAt;
    private LocalDateTime deliveredAt;

    private String specialInstructions;

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum LoadStatus {
        PENDING, ASSIGNED, IN_TRANSIT, DELIVERED, CANCELLED
    }
}
