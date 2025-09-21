package services;

import entities.Instructor;
import exceptions.InvalidInstructorException;
import exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import repositories.InstructorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InstuctorServiceImpl implements InstructorService{

    private final InstructorRepository instructorRepository;

    public InstuctorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public Instructor addInstructor(Instructor instructor) {
        if (instructor.getName() == null || instructor.getName().isBlank()) {
            throw new InvalidInstructorException("Firstname cant be empty");
        }
        if (instructor.getSpeciality() == null || instructor.getSpeciality().isBlank()) {
            throw new InvalidInstructorException("Speciality cant be empty");
        }
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor updateInstructor(Long id, Instructor instructor) {
        Instructor existingInstructor = instructorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Instructor", "id", id));
        if (instructor.getName() != null || instructor.getName().isBlank()) {
            existingInstructor.setName(instructor.getName());
        }
        if (instructor.getSpeciality() != null || instructor.getSpeciality().isBlank()) {
            existingInstructor.setSpeciality(instructor.getSpeciality());
        }
        return instructorRepository.save(existingInstructor);
    }

    @Override
    public void deleteInstructor(Long id) {
        Instructor existingInstructor = instructorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Instructor", "id", id));
        instructorRepository.delete(existingInstructor);
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Optional<Instructor> getInstructorById(Long id) {
        return instructorRepository.findById(id);
    }

    @Override
    public List<Instructor> getInstructorsBySpeciality(String speciality) {
        return instructorRepository.findBySpecialityIgnoreCase(speciality);
    }
}
