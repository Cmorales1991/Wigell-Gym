package org.example.wigellgym.repositories;

import org.example.wigellgym.entities.GymBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GymBookingRepository extends JpaRepository<GymBooking, Long> {

    List<GymBooking> findAllByCustomerUsername(String user);
    List<GymBooking> findByCancelledTrue();
    List<GymBooking> findByBookingDateAfter(LocalDate bookingDate);
    List<GymBooking> findByBookingDateBefore(LocalDate bookingDate);
}
