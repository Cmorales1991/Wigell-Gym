package entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private boolean cancelled;

    @Column(nullable = false)
    private double totalPriceSEK;

    @Transient
    private double totalPriceEURO;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Workout workOut;

    public Booking() {
    }

    public Booking(Long id, LocalDate bookingDate, boolean cancelled, double totalPriceSEK, double totalPriceEURO, Customer customer, Workout workOut) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.cancelled = false;
        this.totalPriceSEK = totalPriceSEK;
        this.totalPriceEURO = totalPriceEURO;
        this.customer = customer;
        this.workOut = workOut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public double getTotalPriceSEK() {
        return totalPriceSEK;
    }

    public void setTotalPriceSEK(double totalPriceSEK) {
        this.totalPriceSEK = totalPriceSEK;
    }

    public double getTotalPriceEURO() {
        return totalPriceEURO;
    }

    public void setTotalPriceEURO(double totalPriceEURO) {
        this.totalPriceEURO = totalPriceEURO;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Workout getWorkOut() {
        return workOut;
    }

    public void setWorkOut(Workout workOut) {
        this.workOut = workOut;
    }
}
