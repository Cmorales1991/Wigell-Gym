package org.example.wigellgym.repositories;

import org.example.wigellgym.entities.GymWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymWorkoutRepository extends JpaRepository<GymWorkout, Long> {

    List<GymWorkout> findByInstructorId(Long id);
}
