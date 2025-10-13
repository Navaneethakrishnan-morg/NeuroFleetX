package com.neurofleetx.repository;

import com.neurofleetx.model.Trip;
import com.neurofleetx.model.User;
import com.neurofleetx.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByDriver(User driver);
    List<Trip> findByVehicle(Vehicle vehicle);
    List<Trip> findByStatus(Trip.TripStatus status);
    List<Trip> findByDriverOrderByStartTimeDesc(User driver);
}
