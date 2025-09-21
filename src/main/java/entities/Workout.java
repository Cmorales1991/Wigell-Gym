package entities;

import jakarta.persistence.*;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String workoutName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double duration;

    @Column(nullable = false)
    private double maxParticipants;

    @Column(nullable = false)
    private double priceSEK;

    @Transient
    private double priceEURO;

    @ManyToOne
    private Instructor instructor;

    public Workout() {
    }

    public Workout(Long id, String workoutName, String type, double duration, double maxParticipants, double priceSEK, double priceEURO, Instructor instructor) {
        this.id = id;
        this.workoutName = workoutName;
        this.type = type;
        this.duration = duration;
        this.maxParticipants = maxParticipants;
        this.priceSEK = priceSEK;
        this.priceEURO = priceEURO;
        this.instructor = instructor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(double maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public double getPriceSEK() {
        return priceSEK;
    }

    public void setPriceSEK(double priceSEK) {
        this.priceSEK = priceSEK;
    }

    public double getPriceEURO() {
        return priceEURO;
    }

    public void setPriceEURO(double priceEURO) {
        this.priceEURO = priceEURO;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
