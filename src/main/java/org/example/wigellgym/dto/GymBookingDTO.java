package org.example.wigellgym.dto;

import org.example.wigellgym.entities.GymBooking;

import java.time.LocalDate;

public class GymBookingDTO {
    private Long id;
    private LocalDate bookingDate;
    private boolean cancelled;
    private double totalPriceSEK;
    private double totalPriceEURO;
    private String workoutName;
    private String workoutType;
    private String instructorName;
    private String customerName;

    public GymBookingDTO(GymBooking booking) {
        this.id = booking.getId();
        this.bookingDate = booking.getBookingDate();
        this.cancelled = booking.isCancelled();
        this.totalPriceSEK = booking.getTotalPriceSEK();
        this.totalPriceEURO = booking.getTotalPriceEURO();

        if (booking.getWorkout() != null) {
            this.workoutName = booking.getWorkout().getWorkoutName();
            this.workoutType = booking.getWorkout().getType();
            if (booking.getWorkout().getInstructor() != null) {
                this.instructorName = booking.getWorkout().getInstructor().getName();
            }
        }
        if (booking.getCustomer() != null) {
            this.customerName = booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName();
        }
    }

    public Long getId() { return id; }
    public LocalDate getBookingDate() { return bookingDate; }
    public boolean isCancelled() { return cancelled; }
    public double getTotalPriceSEK() { return totalPriceSEK; }
    public double getTotalPriceEURO() { return totalPriceEURO; }
    public String getWorkoutName() { return workoutName; }
    public String getWorkoutType() { return workoutType; }
    public String getInstructorName() { return instructorName; }
    public String getCustomerName() { return customerName; }
}
