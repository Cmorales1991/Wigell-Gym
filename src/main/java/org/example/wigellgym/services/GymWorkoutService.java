package org.example.wigellgym.services;

import org.example.wigellgym.entities.GymWorkout;

import java.util.List;

public interface GymWorkoutService {

    GymWorkout addWorkout(GymWorkout gymWorkout);
    GymWorkout updateWorkout(Long id, GymWorkout gymWorkout);
    void deleteWorkout(Long id);
    List<GymWorkout> getAllWorkout();
    List<GymWorkout> getWorkoutByInstructor(Long instructorId);
    GymWorkout getWorkoutById(Long id);
}
