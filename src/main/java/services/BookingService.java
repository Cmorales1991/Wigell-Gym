package services;

import entities.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking addBooking(Booking booking);
    Booking updateBooking(Long id, Booking booking);
    boolean cancelBooking(Long id);
    List<Booking> getAllBookings();
    List<Booking> getCanceledBookings();
    List<Booking> getUpcomingBookings();
    List<Booking> getPastBookings();
    List<Booking> getBookingsByEmail(String email);
}
