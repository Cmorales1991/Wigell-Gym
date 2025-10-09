package org.example.wigellgym.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class GymBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_date" , nullable = false)
    private LocalDate bookingDate;

    @Column(name ="cancelled",nullable = false)
    private boolean cancelled;

    @Column(name="total_price_sek",nullable = false)
    private double totalPriceSEK;

    @Transient
    private double totalPriceEURO;

    @JsonIgnoreProperties("bookings")
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private GymCustomer customer;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private GymWorkout workout;

    public GymBooking() {}

    public GymBooking(Long id, LocalDate bookingDate, boolean cancelled, double totalPriceSEK, double totalPriceEURO, GymCustomer customer, GymWorkout workout) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.cancelled = cancelled;
        this.totalPriceSEK = totalPriceSEK;
        this.totalPriceEURO = totalPriceEURO;
        this.customer = customer;
        this.workout = workout;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public double getTotalPriceSEK() { return totalPriceSEK; }
    public void setTotalPriceSEK(double totalPriceSEK) { this.totalPriceSEK = totalPriceSEK; }

    public double getTotalPriceEURO() { return totalPriceEURO; }
    public void setTotalPriceEURO(double totalPriceEURO) { this.totalPriceEURO = totalPriceEURO; }

    public GymCustomer getCustomer() { return customer; }
    public void setCustomer(GymCustomer customer) { this.customer = customer; }

    public GymWorkout getWorkout() { return workout; }
    public void setWorkout(GymWorkout workout) { this.workout = workout; }
}
