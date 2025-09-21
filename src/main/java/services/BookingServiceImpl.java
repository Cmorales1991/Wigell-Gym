package services;


import entities.Booking;
import entities.Customer;
import entities.Workout;
import exceptions.InvalidBookingException;
import exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import repositories.BookingRepository;
import repositories.CustomerRepository;
import repositories.WorkoutRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final WorkoutRepository workoutRepository;
    private final double conversionCourse = 11.06; // dagens euro kurs

    public BookingServiceImpl(BookingRepository bookingRepository,
                              CustomerRepository customerRepository, WorkoutRepository workoutRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.workoutRepository = workoutRepository;
    }

    private void convertPrice(Booking booking) {
        booking.setTotalPriceEURO(booking.getTotalPriceSEK() / conversionCourse);
    }

    @Override
    public Booking addBooking(Booking booking) {
        if(booking.getCustomer() == null || booking.getCustomer().getId() == null ) {
            throw new InvalidBookingException("Customer id must be provided");
        }
        if(booking.getWorkOut() == null || booking.getWorkOut().getId() == null) {
            throw new InvalidBookingException("Workout id must be provided");
        }
        Customer customer = customerRepository.findById(booking.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", booking.getCustomer().getId()));
        Workout workout = workoutRepository.findById(booking.getWorkOut().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout", "id", booking.getWorkOut().getId()));

        booking.setCustomer(customer);
        booking.setWorkOut(workout);
        booking.setTotalPriceSEK(workout.getPriceSEK());
        booking.setCancelled(false);
        convertPrice(booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        // en kontroll likt cancel booking där en kund inte kan ändra bokning 1 dag innan
        LocalDate today=LocalDate.now();
        if (!existing.getBookingDate().isAfter(today.plusDays(1))) {
            throw new InvalidBookingException("Cant update booking within one day of the booking date");
        }

        if(booking.getCustomer() != null && booking.getCustomer().getId() != null ) {
            Customer customer = customerRepository.findById(booking.getCustomer().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", booking.getCustomer().getId()));
            existing.setCustomer(customer);
        }

        if(booking.getWorkOut() != null || booking.getWorkOut().getId() != null) {
            Workout workout = workoutRepository.findById(booking.getWorkOut().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Workout", "id", booking.getWorkOut().getId()));
            existing.setWorkOut(workout);
            existing.setTotalPriceSEK(workout.getPriceSEK());
        }

        if (booking.getBookingDate() != null) {
            existing.setBookingDate(booking.getBookingDate());
        }

        existing.setBookingDate(booking.getBookingDate());
        existing.setCancelled(booking.isCancelled());

        convertPrice(existing);
        return bookingRepository.save(existing);
    }

    @Override
    public boolean cancelBooking(Long id) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("booking", "id", id));

        // kontroll för avbokning fram till en dag innan
        LocalDate today = LocalDate.now();
        if (!existing.getBookingDate().isAfter(today.plusDays(1))) {
            throw new InvalidBookingException("Cant cancel booking within one day of the booking date");
        }

        existing.setCancelled(true);
        bookingRepository.save(existing);
        return true;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        bookings.forEach(this::convertPrice);
        return bookings;
    }

    @Override
    public List<Booking> getCanceledBookings() {
        List<Booking> bookings = bookingRepository.findByCancelledTrue();
        bookings.forEach(this::convertPrice);
        return bookings;
    }

    @Override
    public List<Booking> getUpcomingBookings() {
        List<Booking> bookings = bookingRepository.findByBookingDateAfter(LocalDate.now());
        bookings.forEach(this::convertPrice);
        return bookings;
    }

    @Override
    public List<Booking> getPastBookings() {
        List<Booking> bookings = bookingRepository.findByBookingDateBefore(LocalDate.now());
        bookings.forEach(this::convertPrice);
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        List<Booking> bookings = bookingRepository.findAllByCustomerEmail(customer.getEmail());
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("Booking", "email", email);
        }
        bookings.forEach(this::convertPrice);
        return bookings;
    }
}
