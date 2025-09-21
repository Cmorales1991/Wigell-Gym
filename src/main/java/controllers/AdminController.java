package controllers;

import entities.Booking;
import entities.Customer;
import entities.Instructor;
import entities.Workout;
import exceptions.InvalidBookingException;
import exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.BookingService;
import services.CustomerService;
import services.InstructorService;
import services.WorkoutService;

import java.util.List;

@RestController
@RequestMapping("/api/wigellgym")
public class AdminController {

    private final BookingService bookingService;
    private final WorkoutService workoutService;
    private final InstructorService instructorService;
    private final CustomerService customerService;

    public AdminController(BookingService bookingService,
                           WorkoutService workoutService,
                           InstructorService instructorService, CustomerService customerService) {
        this.bookingService = bookingService;
        this.workoutService = workoutService;
        this.instructorService = instructorService;
        this.customerService = customerService;
    }

    // Booking end points för endast admin
    @GetMapping("/listcanceled")
    public List<Booking> getCanceledBookings() {
        return bookingService.getCanceledBookings();
    }

    @GetMapping("/listupcoming")
    public List<Booking> getUpcomingBookings() {
        return bookingService.getUpcomingBookings();
    }

    @GetMapping("/listpast")
    public List<Booking> getPastBookings() {
        return bookingService.getPastBookings();
    }
    //en endpoint för admin om kund vill göra ändring i sin bokning.
    @PutMapping("/bookings/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        try {
            Booking updated = bookingService.updateBooking(id, booking);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException | InvalidBookingException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //work out endpoints
    @PostMapping("/addworkout")
    public ResponseEntity<?> addWorkout(@RequestBody Workout workout) {
        try {
            Workout saved = workoutService.addWorkout(workout);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (ResourceNotFoundException | InvalidBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/updateworkout/{id}")
    public ResponseEntity<?> updateWorkout(@PathVariable Long id, @RequestBody Workout workout) {
        try {
            Workout updated = workoutService.updateWorkout(id, workout);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException | InvalidBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/remworkout/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable Long id) {
        try {
            workoutService.deleteWorkout(id);
            return ResponseEntity.ok("Workout deleted successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Instructors endpoint
    @PostMapping("/addinstructor")
    public ResponseEntity<?> addInstructor(@RequestBody Instructor instructor) {
        try {
            Instructor saved = instructorService.addInstructor(instructor);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (InvalidBookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    @PutMapping("/updateinstructor/{id}")
    public Instructor updateInstructor(@PathVariable Long id, @RequestBody Instructor instructor) {
        return instructorService.updateInstructor(id, instructor);
    }

    @DeleteMapping("/reminstructor/{id}")
    public void deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
    }

    //customer endpoints
    @PostMapping("/addcustomer")
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/updatecustomer/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/remcustomer/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers/email")
    public Customer getCustomerByEmail(@RequestParam String email) {
        return customerService.getCustomerByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
    }
}