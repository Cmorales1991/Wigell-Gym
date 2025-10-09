package org.example.wigellgym.services;

import org.example.wigellgym.dto.GymBookingDTO;
import org.example.wigellgym.entities.GymBooking;

import java.security.Principal;
import java.util.List;

public interface GymBookingService {

    GymBooking addBooking(GymBooking gymBooking);
    GymBooking addBookingForUser(GymBooking gymBooking);
    GymBooking updateBooking(Long id, GymBooking gymBooking);
    GymBooking updateBookingForUser(Long id, GymBooking gymBooking, String user);
    boolean cancelBooking(Long id);
    boolean cancelBookingForUser(Long id, String user);
    List<GymBooking> getAllBookings();
    List<GymBooking> getCanceledBookings();
    List<GymBooking> getUpcomingBookings();
    List<GymBooking> getPastBookings();
    List<GymBookingDTO> getBookingsByUsername();
}
