package org.example.wigellgym.services;


import org.example.wigellgym.dto.GymBookingDTO;
import org.example.wigellgym.entities.GymBooking;
import org.example.wigellgym.entities.GymCustomer;
import org.example.wigellgym.entities.GymWorkout;
import org.example.wigellgym.exceptions.InvalidGymBookingException;
import org.example.wigellgym.exceptions.GymResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.wigellgym.repositories.GymBookingRepository;
import org.example.wigellgym.repositories.GymCustomerRepository;
import org.example.wigellgym.repositories.GymWorkoutRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class GymBookingServiceImpl implements GymBookingService {

    private final GymBookingRepository gymBookingRepository;
    private final GymCustomerRepository gymCustomerRepository;
    private final GymWorkoutRepository workoutRepository;
    private final double conversionCourse = 11.06; // dagens euro kurs
    private static final Logger logger = LoggerFactory.getLogger(GymBookingServiceImpl.class);

    public GymBookingServiceImpl(GymBookingRepository gymBookingRepository, GymCustomerRepository gymCustomerRepository, GymWorkoutRepository workoutRepository) {
        this.gymBookingRepository = gymBookingRepository;
        this.gymCustomerRepository = gymCustomerRepository;
        this.workoutRepository = workoutRepository;
    }

    private void convertPrice(GymBooking gymBooking) {
        gymBooking.setTotalPriceEURO(gymBooking.getTotalPriceSEK() / conversionCourse);
    }

    @Override
    public GymBooking addBooking(GymBooking gymBooking) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminUser = authentication.getName();

            logger.info("User {} is attempting to add a booking", adminUser);
        if(gymBooking.getCustomer() == null || gymBooking.getCustomer().getId() == null ) {
            logger.warn("User '{}' failed to add booking: customer id missing", adminUser);
            throw new InvalidGymBookingException("Customer id must be provided");
        }
        if(gymBooking.getWorkout() == null || gymBooking.getWorkout().getId() == null) {
            logger.warn("User '{}' failed to add booking: workout id missing", adminUser);
            throw new InvalidGymBookingException("Workout id must be provided");
        }
        GymCustomer gymCustomer = gymCustomerRepository.findById(gymBooking.getCustomer().getId())
                .orElseThrow(() -> new GymResourceNotFoundException("Customer", "id", gymBooking.getCustomer().getId()));
        GymWorkout gymWorkout = workoutRepository.findById(gymBooking.getWorkout().getId())
                .orElseThrow(() -> new GymResourceNotFoundException("Workout", "id", gymBooking.getWorkout().getId()));

        gymBooking.setCustomer(gymCustomer);
        gymBooking.setWorkout(gymWorkout);
        gymBooking.setTotalPriceSEK(gymWorkout.getPriceSEK());
        gymBooking.setCancelled(false);
        convertPrice(gymBooking);

        GymBooking saved = gymBookingRepository.save(gymBooking);
        logger.info("User '{}' successfully added booking with id {}", adminUser, saved.getId());
        return saved;
    }

    @Override
    public GymBooking addBookingForUser(GymBooking gymBooking) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to book a workout", username);
        GymCustomer gymCustomer = gymCustomerRepository.findByUsername(username)
                .orElseThrow(() -> new GymResourceNotFoundException("Customer","user", username));

        if(gymBooking.getWorkout() == null || gymBooking.getWorkout().getId() == null ) {
            logger.warn("User '{}' failed to add booking: workout id missing", username);
            throw new InvalidGymBookingException("Workout id must be provided");
        }
        GymWorkout gymWorkout = workoutRepository.findById(gymBooking.getWorkout().getId())
                .orElseThrow(()-> new GymResourceNotFoundException("Workout", "id", gymBooking.getWorkout().getId()));

        gymBooking.setCustomer(gymCustomer);
        gymBooking.setWorkout(gymWorkout);
        gymBooking.setTotalPriceSEK(gymWorkout.getPriceSEK());
        gymBooking.setCancelled(false);
        convertPrice(gymBooking);

        return gymBookingRepository.save(gymBooking);
    }

    @Override
    public GymBooking updateBooking(Long id, GymBooking gymBooking) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to update a booking", username);
        GymBooking existing = gymBookingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Booking with id {} not found", id);
                    return new GymResourceNotFoundException("Booking", "id", id);
                });

        // en kontroll likt cancel booking där en kund inte kan ändra bokning 1 dag innan
        LocalDate today=LocalDate.now();
        if (!existing.getBookingDate().isAfter(today.plusDays(1))) {
            logger.warn("User '{}' is attempting to update booking to close on booking date", username);
            throw new InvalidGymBookingException("Cant update booking within one day of the booking date");
        }
        if(gymBooking.getCustomer() != null && gymBooking.getCustomer().getId() != null ) {
            GymCustomer gymCustomer = gymCustomerRepository.findById(gymBooking.getCustomer().getId())
                    .orElseThrow(() -> {
                        logger.warn("Customer with id {} not found", gymBooking.getCustomer().getId());
                        return new GymResourceNotFoundException("Customer", "id", gymBooking.getCustomer().getId());
                    });
            existing.setCustomer(gymCustomer);
        }
        if(gymBooking.getWorkout() != null && gymBooking.getWorkout().getId() != null) {
            GymWorkout gymWorkout = workoutRepository.findById(gymBooking.getWorkout().getId())
                    .orElseThrow(() -> {
                        logger.warn("Workout with id {} not found", gymBooking.getWorkout().getId());
                        return new GymResourceNotFoundException("Workout", "id", gymBooking.getWorkout().getId());
                    });
            existing.setWorkout(gymWorkout);
            existing.setTotalPriceSEK(gymWorkout.getPriceSEK());
            logger.info("User '{}' updated workout for booking id {} to workout '{}'", username, id, gymWorkout.getWorkoutName());
        }
        if (gymBooking.getBookingDate() != null) {
            existing.setBookingDate(gymBooking.getBookingDate());
            logger.info("User '{}' updated booking date for booking with id {} to {}", username, id, existing.getBookingDate());
        }

        existing.setCancelled(gymBooking.isCancelled());
        logger.info("User '{}' updated booking with id '{}' is cancelled", username, gymBooking.getId());

        convertPrice(existing);
        GymBooking updated = gymBookingRepository.save(existing);
        logger.info("User '{}' successfully updated booking id {}", username, updated.getId());
        return updated;
    }

    @Override
    public GymBooking updateBookingForUser(Long id, GymBooking gymBooking, String user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to update booking with id {}", username, id);
        GymBooking existing = gymBookingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Booking with id {} not found for user '{}'", id, username);
                    return new GymResourceNotFoundException("Booking", "id", id);
                });
        if (!existing.getCustomer().getUsername().equals(user)) {
            logger.warn("User '{}' attempted to update booking id {} that belongs to another user '{}'",
                    user, id, existing.getCustomer().getUsername());
            throw new InvalidGymBookingException("You can only update your own booking");
        }

        // en kontroll likt cancel booking där en kund inte kan ändra bokning 1 dag innan
        LocalDate today=LocalDate.now();
        if (!existing.getBookingDate().isAfter(today.plusDays(1))) {
            logger.warn("User '{}' attempted to update booking id {} too close to booking date", username, id);
            throw new InvalidGymBookingException("Cant update booking within one day of the booking date");
        }
        if(gymBooking.getWorkout() != null && gymBooking.getWorkout().getId() != null) {
            GymWorkout gymWorkout = workoutRepository.findById(gymBooking.getWorkout().getId())
                    .orElseThrow(() -> {
                        logger.warn("Workout with id {} not found while user '{}' tried updating booking id {}", gymBooking.getWorkout().getId(), username, id);
                        return new GymResourceNotFoundException("Workout", "id", gymBooking.getWorkout().getId());
                    });
            existing.setWorkout(gymWorkout);
            existing.setTotalPriceSEK(gymWorkout.getPriceSEK());
            logger.info("User '{}' updated workout for booking id {} to workout '{}'", username, id, gymWorkout.getWorkoutName());
        }
        if (gymBooking.getBookingDate() != null) {
            existing.setBookingDate(gymBooking.getBookingDate());
            logger.info("User '{}' updated booking date for booking id {} to {}",
                    username, id, gymBooking.getBookingDate());
        }

        existing.setCancelled(gymBooking.isCancelled());
        logger.info("User '{}' updated booking with id '{}' is cancelled", user, gymBooking.getId());

        convertPrice(existing);
        GymBooking updated = gymBookingRepository.save(existing);
        logger.info("User '{}' successfully updated booking id {}", username, updated.getId());
        return updated;
    }

    @Override
    public boolean cancelBooking(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to cancel booking with id {}", username, id);
        GymBooking existing = gymBookingRepository.findById(id)
                .orElseThrow(() ->{
                    logger.warn("User '{}' failed to cancel booking: booking id {} not found", username, id);
                    return new GymResourceNotFoundException("booking", "id", id);
                });
        // kontroll för avbokning fram till en dag innan
        LocalDate today = LocalDate.now();
        if (!existing.getBookingDate().isAfter(today.plusDays(1))) {
            logger.error("User '{}' failed to cancel booking: too late to cancel booking id {}", username, id);
            throw new InvalidGymBookingException("Cant cancel booking within one day of the booking date");
        }

        existing.setCancelled(true);
        gymBookingRepository.save(existing);
        logger.info("User '{}' successfully cancelled booking with id {}", username, id);
        return true;
    }

    @Override
    public boolean cancelBookingForUser(Long id, String user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' is attempting to cancel booking with id {}", username, id);
        GymBooking existing = gymBookingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User '{}' failed to cancel booking: booking id {} not found", username, id);
                    return new GymResourceNotFoundException("Booking", "id", id);
                });

        if (!existing.getCustomer().getUsername().equals(username)) {
            logger.warn("User '{}' attempted to cancel booking id {} not belonging to them", user, id);
            throw new InvalidGymBookingException("You can only cancel your own bookings");
        }

        LocalDate today = LocalDate.now();
        if (!existing.getBookingDate().isAfter(today.plusDays(1))) {
            logger.warn("User '{}' failed to cancel booking: too late to cancel booking id {}", user, id);
            throw new InvalidGymBookingException("Cannot cancel booking within one day of the booking date");
        }

        existing.setCancelled(true);
        gymBookingRepository.save(existing);
        logger.info("User '{}' successfully cancelled their booking with id {}", user, id);
        return true;
    }

    @Override
    public List<GymBooking> getAllBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("User '{}' requested all bookings", username);
        List<GymBooking> gymBookings = gymBookingRepository.findAll();
        if (gymBookings.isEmpty()) {
            logger.warn("User '{}' found no bookings in the system", username);
        } else {
            logger.info("User '{}' retrieved {} bookings", username, gymBookings.size());
        }
        gymBookings.forEach(this::convertPrice);
        return gymBookings;
    }

    @Override
    public List<GymBooking> getCanceledBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("User '{}' requested all canceled bookings", username);
        List<GymBooking> gymBookings = gymBookingRepository.findByCancelledTrue();

        if (gymBookings.isEmpty()) {
            logger.warn("User '{}' found no canceled bookings", username);
        } else {
            logger.info("User '{}' retrieved {} canceled bookings", username, gymBookings.size());
        }
        gymBookings.forEach(this::convertPrice);
        return gymBookings;
    }

    @Override
    public List<GymBooking> getUpcomingBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("User '{}' requested all upcoming bookings", username);
        List<GymBooking> gymBookings = gymBookingRepository.findByBookingDateAfter(LocalDate.now());

        if (gymBookings.isEmpty()) {
            logger.warn("User '{}' found no upcoming bookings", username);
        } else {
            logger.info("User '{}' retrieved {} upcoming bookings", username, gymBookings.size());
        }        gymBookings.forEach(this::convertPrice);
        return gymBookings;
    }

    @Override
    public List<GymBooking> getPastBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("User '{}' requested all past bookings", username);
        List<GymBooking> gymBookings = gymBookingRepository.findByBookingDateBefore(LocalDate.now());

        if (gymBookings.isEmpty()) {
            logger.warn("User '{}' found no past bookings", username);
        } else {
            logger.info("User '{}' retrieved {} past bookings", username, gymBookings.size());
        }        gymBookings.forEach(this::convertPrice);
        return gymBookings;
    }

    @Override
    public List<GymBookingDTO> getBookingsByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("User '{}' requested to see their bookings", username);

        GymCustomer gymCustomer = gymCustomerRepository.findByUsername(username)
                .orElseThrow(() -> new GymResourceNotFoundException("Customer", "user", username));

        List<GymBooking> bookings = gymBookingRepository.findAllByCustomerUsername(gymCustomer.getUsername());

        if (bookings.isEmpty()) {
            logger.warn("User '{}' found no bookings for customer with username '{}'", username);
            throw new GymResourceNotFoundException("Booking", "user", username);
        } else {
        logger.info("User '{}' retrieved {} bookings for customer '{}'", username, bookings.size());
        }

        bookings.forEach(this::convertPrice);

        return bookings.stream()
                .map(GymBookingDTO::new)
                .toList();
    }
}
