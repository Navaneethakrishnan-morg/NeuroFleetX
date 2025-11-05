package com.neurofleetx.repository;

import com.neurofleetx.model.User;
import com.neurofleetx.model.Vehicle;
import com.neurofleetx.model.VehicleRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRatingRepository extends JpaRepository<VehicleRating, Long> {
    List<VehicleRating> findByCustomer(User customer);
    List<VehicleRating> findByVehicle(Vehicle vehicle);
    
    @Query("SELECT AVG(r.rating) FROM VehicleRating r WHERE r.vehicle = :vehicle")
    Double findAverageRatingByVehicle(Vehicle vehicle);
}
