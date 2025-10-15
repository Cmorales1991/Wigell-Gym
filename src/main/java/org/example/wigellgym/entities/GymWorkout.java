package org.example.wigellgym.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "workout")
public class GymWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="workout_name", nullable = false)
    private String workoutName;

    @Column(name="type", nullable = false)
    private String type;

    @Column(name="duration", nullable = false)
    private int duration;

    @Column(name="max_participants", nullable = false)
    private int maxParticipants;

    @Column(name="price_sek", nullable = false)
    private double priceSEK;

    @Transient
    private double priceEURO;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private GymInstructor instructor;

    @JsonIgnoreProperties("workout")
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GymBooking> bookings;

    public GymWorkout() {}

    public GymWorkout(Long id, String workoutName, String type, int duration, int maxParticipants, double priceSEK/*, double priceEURO*/, GymInstructor instructor) {
        this.id = id;
        this.workoutName = workoutName;
        this.type = type;
        this.duration = duration;
        this.maxParticipants = maxParticipants;
        this.priceSEK = priceSEK;
        //this.priceEURO = priceEURO;
        this.instructor = instructor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public double getPriceSEK() { return priceSEK; }
    public void setPriceSEK(double priceSEK) { this.priceSEK = priceSEK; }

    public double getPriceEURO() { return priceEURO; }
    public void setPriceEURO(double priceEURO) { this.priceEURO = priceEURO; }

    public GymInstructor getInstructor() { return instructor; }
    public void setInstructor(GymInstructor instructor) { this.instructor = instructor; }

    public List<GymBooking> getBookings() { return bookings; }
    public void setBookings(List<GymBooking> bookings) { this.bookings = bookings; }
}
