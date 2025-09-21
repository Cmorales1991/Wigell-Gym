package services;

import entities.Workout;
import exceptions.InvalidWorkoutException;
import exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import repositories.WorkoutRepository;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final double conversionCourse = 11.06; // dagens euro kurs

    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    // valutakonvertering
    private void convertPrice(Workout workout) {
        workout.setPriceEURO(workout.getPriceSEK() / conversionCourse);
    }

    @Override
    public Workout addWorkout(Workout workout) {
        if (workout.getWorkoutName() == null || workout.getWorkoutName().isBlank()) {
            throw new InvalidWorkoutException("Workout name is required");
        }
        if (workout.getType() == null || workout.getType().isBlank()) {
            throw new InvalidWorkoutException("Workout type is required");
        }
        if (workout.getPriceSEK() <= 0) {
            throw new InvalidWorkoutException("Workout price must be greater than 0");
        }
        if (workout.getMaxParticipants() <= 0) {
            throw new InvalidWorkoutException("Workout must allow at least 1 participant");
        }

        convertPrice(workout);
        return workoutRepository.save(workout);
    }

    @Override
    public Workout updateWorkout(Long id, Workout workout) {
        Workout existing = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout", "id", id));

        if (workout.getWorkoutName() != null && !workout.getWorkoutName().isBlank()) {
            existing.setWorkoutName(workout.getWorkoutName());
        }
        if (workout.getType() != null && !workout.getType().isBlank()) {
            existing.setType(workout.getType());
        }
        if (workout.getPriceSEK() > 0) {
            existing.setPriceSEK(workout.getPriceSEK());
        }
        if (workout.getMaxParticipants() > 0) {
            existing.setMaxParticipants(workout.getMaxParticipants());
        }
        if (workout.getInstructor() != null) {
            existing.setInstructor(workout.getInstructor());
        }

        convertPrice(existing);
        return workoutRepository.save(existing);
    }

    @Override
    public void deleteWorkout(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout", "id", id));
        workoutRepository.delete(workout);
    }

    @Override
    public List<Workout> getAllWorkout() {
        List<Workout> workout = workoutRepository.findAll();
        workout.forEach(this::convertPrice);
        return workout;
    }

    @Override
    public List<Workout> getWorkoutByInstructor(Long instructorId) {
        return workoutRepository.findByInstructorId(instructorId);
    }

    @Override
    public Workout getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout", "id", id));
        convertPrice(workout);
        return workout;
    }
}
