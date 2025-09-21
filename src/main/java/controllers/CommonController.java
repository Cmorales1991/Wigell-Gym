package controllers;

import entities.Booking;
import entities.Instructor;
import entities.Workout;
import exceptions.InvalidBookingException;
import exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.BookingService;
import services.InstructorService;
import services.WorkoutService;

import java.util.List;

@RestController
@RequestMapping("/api/wigellgym")
public class CommonController {

    private final WorkoutService workoutService;
    private final InstructorService instructorService;
    private final BookingService bookingService;

    public CommonController(WorkoutService workoutService, InstructorService instructorService, BookingService bookingService) {
        this.workoutService = workoutService;
        this.instructorService = instructorService;
        this.bookingService = bookingService;

    }
    // end points för både admin o users
    // instrucors endpoints
    @GetMapping("/instructors")
    public List<Instructor> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    @GetMapping("/instructors/{id}")
    public Instructor getInstructorById(@PathVariable Long id) {
        return instructorService.getInstructorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor", "id", id));
    }

    @GetMapping("/instructors/speciality")
    public List<Instructor> getInstructorsBySpeciality(@RequestParam String speciality) {
        List<Instructor> instructors = instructorService.getInstructorsBySpeciality(speciality);
        if (instructors.isEmpty()) {
            throw new ResourceNotFoundException("Instructor", "speciality", speciality);
        }
        return instructors;
    }

    //workout endpoints
    @GetMapping("/workout")
    public ResponseEntity<?> getAllWorkout() {
        List<Workout> workout = workoutService.getAllWorkout();
        return ResponseEntity.ok(workout);
    }

    @GetMapping("/workout/{id}")
    public Workout getWorkoutById(@PathVariable Long id) {
        return workoutService.getWorkoutById(id);
    }
    @GetMapping("/workout/instructor/{instructorId}")
    public List<Workout> getWorkoutsByInstructor(@PathVariable Long instructorId) {
        List<Workout> workouts = workoutService.getWorkoutByInstructor(instructorId);
        if(workouts.isEmpty()) {
            throw new ResourceNotFoundException("Workout", "instructorId", instructorId);
        }
        return workouts;
    }

    @PostMapping("/bookworkout")
    public ResponseEntity<?> addBooking(@RequestBody Booking booking) {
        try {
            Booking saved = bookingService.addBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (InvalidBookingException | ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/cancelworkout/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (InvalidBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/mybookings")
    public ResponseEntity<?> getBookingsByEmail(@RequestParam String email) {
        try {
            List<Booking> bookings = bookingService.getBookingsByEmail(email);
            return ResponseEntity.ok(bookings);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}