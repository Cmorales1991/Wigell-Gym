package org.example.wigellgym.dto;

import org.example.wigellgym.entities.GymWorkout;

public class GymWorkoutDTO {
    private Long id;
    private String workoutName;
    private String type;
    private int duration;
    private int maxParticipants;
    private double priceSEK;
    private double priceEURO;
    private String instructorName;

    public GymWorkoutDTO(GymWorkout workout) {
        this.id = workout.getId();
        this.workoutName = workout.getWorkoutName();
        this.type = workout.getType();
        this.duration = workout.getDuration();
        this.maxParticipants = workout.getMaxParticipants();
        this.priceSEK = workout.getPriceSEK();
        this.priceEURO = workout.getPriceEURO();
        if (workout.getInstructor() != null) {
            this.instructorName = workout.getInstructor().getName();
        }
    }

    public Long getId() { return id; }
    public String getWorkoutName() { return workoutName; }
    public String getType() { return type; }
    public int getDuration() { return duration; }
    public int getMaxParticipants() { return maxParticipants; }
    public double getPriceSEK() { return priceSEK; }
    public double getPriceEURO() { return priceEURO; }
    public String getInstructorName() { return instructorName; }

}
