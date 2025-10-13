package com.neurofleetx.repository;

import com.neurofleetx.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
    List<Vehicle> findByType(Vehicle.VehicleType type);
    List<Vehicle> findByIsElectric(Boolean isElectric);
}
