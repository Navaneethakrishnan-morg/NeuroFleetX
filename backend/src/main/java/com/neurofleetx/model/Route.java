package com.neurofleetx.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long routeId;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private String startLocation;

    @Column(nullable = false)
    private String endLocation;

    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;

    @Column(nullable = false)
    private Double distanceKm;

    @Column(nullable = false)
    private Integer etaMinutes;

    private Double energyCost;

    @Enumerated(EnumType.STRING)
    private TrafficLevel trafficLevel = TrafficLevel.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptimizationType optimizationType;

    @Column(columnDefinition = "TEXT")
    private String optimizedPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteStatus status = RouteStatus.PENDING;

    private Integer priority = 5;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    private LocalDateTime completedAt;

    public enum TrafficLevel {
        LOW, MEDIUM, HIGH, SEVERE
    }

    public enum OptimizationType {
        FASTEST, ENERGY_EFFICIENT, BALANCED, SHORTEST
    }

    public enum RouteStatus {
        PENDING, ACTIVE, COMPLETED, CANCELLED
    }
}
