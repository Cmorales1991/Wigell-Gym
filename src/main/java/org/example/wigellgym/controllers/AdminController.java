package org.example.wigellgym.controllers;

import org.example.wigellgym.dto.GymBookingDTO;
import org.example.wigellgym.dto.GymWorkoutDTO;
import org.example.wigellgym.entities.GymCustomer;
import org.example.wigellgym.entities.GymInstructor;
import org.example.wigellgym.entities.GymWorkout;
import org.example.wigellgym.exceptions.InvalidGymBookingException;
import org.example.wigellgym.exceptions.GymResourceNotFoundException;
import org.example.wigellgym.repositories.GymWorkoutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.wigellgym.services.GymBookingService;
import org.example.wigellgym.services.GymCustomerService;
import org.example.wigellgym.services.GymInstructorService;
import org.example.wigellgym.services.GymWorkoutService;

import java.util.List;

@RestController
@RequestMapping("/api/wigellgym")
public class AdminController {

    private final GymBookingService gymBookingService;
    private final GymWorkoutService gymWorkoutService;
    private final GymInstructorService gymInstructorService;
    private final GymCustomerService gymCustomerService;
    private final GymWorkoutRepository workoutRepository;



    public AdminController(GymBookingService gymBookingService,
                           GymWorkoutService gymWorkoutService,
                           GymInstructorService gymInstructorService, GymCustomerService gymCustomerService, GymWorkoutRepository workoutRepository) {
        this.gymBookingService = gymBookingService;
        this.gymWorkoutService = gymWorkoutService;
        this.gymInstructorService = gymInstructorService;
        this.gymCustomerService = gymCustomerService;
        this.workoutRepository = workoutRepository;

    }

    // Booking end points för endast admin
    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        List<GymBookingDTO> dtos = gymBookingService.getAllBookings()
                .stream()
                .map(GymBookingDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/listcanceled")
        public ResponseEntity<?> getCanceledBookings() {
            List<GymBookingDTO> dtos = gymBookingService.getCanceledBookings()
                    .stream()
                    .map(GymBookingDTO::new)
                    .toList();
            return ResponseEntity.ok(dtos);
    }

    @GetMapping("/listupcoming")
    public ResponseEntity<?> getUpcomingBookings() {
        List<GymBookingDTO> dtos = gymBookingService.getUpcomingBookings()
                .stream()
                .map(GymBookingDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/listpast")
    public ResponseEntity<?> getPastBookings() {
        List<GymBookingDTO> dtos = gymBookingService.getPastBookings()
                .stream()
                .map(GymBookingDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    //work out endpoints
    @PostMapping("/addworkout")
    public ResponseEntity<?> addWorkout(@RequestBody GymWorkout gymWorkout) {
        try {
            GymWorkout saved = gymWorkoutService.addWorkout(gymWorkout);
            return ResponseEntity.status(HttpStatus.CREATED).body(new GymWorkoutDTO(saved));
        } catch (GymResourceNotFoundException | InvalidGymBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/updateworkout/{id}")
    public ResponseEntity<?> updateWorkout(@PathVariable Long id, @RequestBody GymWorkout gymWorkout) {
        try {
            GymWorkout updated = gymWorkoutService.updateWorkout(id, gymWorkout);
            return ResponseEntity.ok(new GymWorkoutDTO(updated));
        } catch (GymResourceNotFoundException | InvalidGymBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/remworkout/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable Long id) {
        try {
            gymWorkoutService.deleteWorkout(id);
            return ResponseEntity.ok("Workout deleted successfully");
        } catch (GymResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Instructors endpoint
    @PostMapping("/addinstructor")
    public ResponseEntity<?> addInstructor(@RequestBody GymInstructor gymInstructor) {
        try {
            GymInstructor saved = gymInstructorService.addInstructor(gymInstructor);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (InvalidGymBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    @PutMapping("/updateinstructor/{id}")
    public GymInstructor updateInstructor(@PathVariable Long id, @RequestBody GymInstructor gymInstructor) {
        return gymInstructorService.updateInstructor(id, gymInstructor);
    }

    @DeleteMapping("/reminstructor/{id}")
    public void deleteInstructor(@PathVariable Long id) {
        gymInstructorService.deleteInstructor(id);
    }

    //customer endpoints
    @PostMapping("/addcustomer")
    public GymCustomer addCustomer(@RequestBody GymCustomer gymCustomer) {
        return gymCustomerService.createCustomer(gymCustomer);
    }

    @PutMapping("/updatecustomer/{id}")
    public GymCustomer updateCustomer(@PathVariable Long id, @RequestBody GymCustomer gymCustomer) {
        return gymCustomerService.updateCustomer(id, gymCustomer);
    }

    @DeleteMapping("/remcustomer/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        gymCustomerService.deleteCustomer(id);
    }

    @GetMapping("/customers")
    public List<GymCustomer> getAllCustomers() {
        return gymCustomerService.getAllCustomers();
    }

    @GetMapping("/customers/username")
    public GymCustomer getCustomerByUsername(@RequestParam String user) {
        return gymCustomerService.getCustomerByUsername(user)
                .orElseThrow(() -> new GymResourceNotFoundException("Customer", "user", user));
    }
}