package com.neurofleetx.repository;

import com.neurofleetx.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByVehicleId(Long vehicleId);
    List<Route> findByStatus(Route.RouteStatus status);
    List<Route> findByOptimizationType(Route.OptimizationType optimizationType);
    
    @Query("SELECT r FROM Route r WHERE r.status = 'ACTIVE' ORDER BY r.priority DESC")
    List<Route> findActiveRoutesByPriority();
    
    @Query("SELECT r FROM Route r ORDER BY r.timestamp DESC")
    List<Route> findAllOrderByTimestampDesc();
}
