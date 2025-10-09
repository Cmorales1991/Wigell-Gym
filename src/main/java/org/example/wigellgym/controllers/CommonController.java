package org.example.wigellgym.controllers;

import org.example.wigellgym.dto.GymWorkoutDTO;
import org.example.wigellgym.dto.GymBookingDTO;
import org.example.wigellgym.entities.GymBooking;
import org.example.wigellgym.entities.GymInstructor;
import org.example.wigellgym.entities.GymWorkout;
import org.example.wigellgym.exceptions.InvalidGymBookingException;
import org.example.wigellgym.exceptions.GymResourceNotFoundException;
import org.example.wigellgym.repositories.GymWorkoutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.example.wigellgym.services.GymBookingService;
import org.example.wigellgym.services.GymInstructorService;
import org.example.wigellgym.services.GymWorkoutService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/wigellgym")
public class CommonController {

    private final GymWorkoutService gymWorkoutService;
    private final GymInstructorService gymInstructorService;
    private final GymBookingService gymBookingService;
    private final GymWorkoutRepository workoutRepository;


    public CommonController(GymWorkoutService gymWorkoutService, GymInstructorService gymInstructorService, GymBookingService gymBookingService, GymWorkoutRepository workoutRepository) {
        this.gymWorkoutService = gymWorkoutService;
        this.gymInstructorService = gymInstructorService;
        this.gymBookingService = gymBookingService;
        this.workoutRepository = workoutRepository;

    }
    // end points för både admin o users
    // instrucors endpoints
    @GetMapping("/instructors")
    public List<GymInstructor> getAllInstructors() {
        return gymInstructorService.getAllInstructors();
    }

    @GetMapping("/instructors/{id}")
    public GymInstructor getInstructorById(@PathVariable Long id) {
        return gymInstructorService.getInstructorById(id)
                .orElseThrow(() -> new GymResourceNotFoundException("Instructor", "id", id));
    }

    @GetMapping("/instructors/speciality")
    public List<GymInstructor> getInstructorBySpeciality(@RequestParam String speciality) {
        List<GymInstructor> gymInstructors = gymInstructorService.getInstructorsBySpeciality(speciality);
        if (gymInstructors.isEmpty()) {
            throw new GymResourceNotFoundException("Instructor", "speciality", speciality);
        }
        return gymInstructors;
    }

    //workout endpoints
    @GetMapping("/workout")
    public ResponseEntity<?> getAllWorkout() {
        List<GymWorkoutDTO> dtos = gymWorkoutService.getAllWorkout()
                .stream()
                .map(GymWorkoutDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/workout/{id}")
    public GymWorkoutDTO  getWorkoutById(@PathVariable Long id) {
        GymWorkout workout = gymWorkoutService.getWorkoutById(id);
        return new GymWorkoutDTO(workout);
    }

    @GetMapping("/workout/instructor/{instructorId}")
    public List<GymWorkoutDTO> getWorkoutsByInstructor(@PathVariable Long instructorId) {
        List<GymWorkout> workouts = gymWorkoutService.getWorkoutByInstructor(instructorId);
        if(workouts.isEmpty()) {
            throw new GymResourceNotFoundException("Workout", "instructorId", instructorId);
        }
        return workouts.stream().map(GymWorkoutDTO::new).toList();
    }

    @PostMapping("/bookworkout")
    public ResponseEntity<?> addBooking(@RequestBody GymBooking gymBooking, Principal principal) {
        try {
            String username = principal.getName();
            boolean isAdmin= false;
            if (principal instanceof Authentication authentication) {
                isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            }

            GymBooking saved;
            if (isAdmin) {
                saved = gymBookingService.addBooking(gymBooking);
            } else {
                saved = gymBookingService.addBookingForUser(gymBooking);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new GymBookingDTO(saved));
        } catch (InvalidGymBookingException | GymResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/cancelworkout/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Principal principal) {
        try {
            String username = principal.getName();
            boolean isAdmin = false;

            if (principal instanceof Authentication authentication) {
                isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            }
            boolean cancelled;
            if (isAdmin) {
                cancelled = gymBookingService.cancelBooking(id);
            } else {
                cancelled = gymBookingService.cancelBookingForUser(id,username);
            }
            if (cancelled) {
                return ResponseEntity.ok("Booking cancelled successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking could not be cancelled");
            }

        } catch (InvalidGymBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (GymResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody GymBooking gymBooking, Principal principal) {
        try {
            String username = principal.getName();
            boolean isAdmin = false;
            if (principal instanceof Authentication authentication) {
                isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            }

            GymBooking updated;
            if (isAdmin) {
                updated = gymBookingService.updateBooking(id, gymBooking);
            } else {
                updated = gymBookingService.updateBookingForUser(id, gymBooking, username);
            }

            return ResponseEntity.ok(new GymBookingDTO(updated));
        } catch (InvalidGymBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (GymResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/mybookings")
    public ResponseEntity<?> getMyBookings(Principal principal) {
        try {
            String username = principal.getName();
            List<GymBookingDTO> gymBookings = gymBookingService.getBookingsByUsername();
            return ResponseEntity.ok(gymBookings);
        } catch (GymResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}