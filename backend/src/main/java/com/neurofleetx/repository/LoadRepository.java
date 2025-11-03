package com.neurofleetx.repository;

import com.neurofleetx.model.Load;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoadRepository extends JpaRepository<Load, Long> {
    List<Load> findByVehicleId(Long vehicleId);
    List<Load> findByStatus(Load.LoadStatus status);
    List<Load> findByPriority(Load.Priority priority);
    List<Load> findByAssignedRouteId(Long routeId);
    
    @Query("SELECT l FROM Load l WHERE l.status = 'PENDING' ORDER BY l.priority DESC, l.createdAt ASC")
    List<Load> findPendingLoadsByPriority();
    
    @Query("SELECT l FROM Load l WHERE l.vehicleId IS NULL AND l.status = 'PENDING'")
    List<Load> findUnassignedLoads();
}
