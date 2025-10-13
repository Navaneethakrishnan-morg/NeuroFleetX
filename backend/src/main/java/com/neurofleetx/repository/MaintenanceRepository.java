package com.neurofleetx.repository;

import com.neurofleetx.model.Maintenance;
import com.neurofleetx.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByVehicle(Vehicle vehicle);
    List<Maintenance> findByStatus(Maintenance.MaintenanceStatus status);
    List<Maintenance> findByPriority(Maintenance.Priority priority);
    List<Maintenance> findByIsPredictive(Boolean isPredictive);
}
