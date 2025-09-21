package services;

import entities.Workout;

import java.util.List;

public interface WorkoutService {

    Workout addWorkout(Workout workout);
    Workout updateWorkout(Long id, Workout workout);
    void deleteWorkout(Long id);
    List<Workout> getAllWorkout();
    List<Workout> getWorkoutByInstructor(Long instructorId);
    Workout getWorkoutById(Long id);
}
