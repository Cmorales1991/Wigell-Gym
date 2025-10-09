package org.example.wigellgym.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "instructor")
public class GymInstructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="speciality", nullable = false)
    private String speciality;

    @JsonIgnoreProperties("instructor")
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GymWorkout> workouts;

    public GymInstructor() {}

    public GymInstructor(Long id, String name, String speciality) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public List<GymWorkout> getWorkouts() { return workouts; }
    public void setWorkouts(List<GymWorkout> workouts) { this.workouts = workouts; }
}
