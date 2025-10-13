package com.neurofleetx.repository;

import com.neurofleetx.model.Booking;
import com.neurofleetx.model.User;
import com.neurofleetx.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(User customer);
    List<Booking> findByVehicle(Vehicle vehicle);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByCustomerOrderByCreatedAtDesc(User customer);
}
