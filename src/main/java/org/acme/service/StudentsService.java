package org.acme.service;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.domain.Students;
import org.acme.domain.University;
import org.acme.dto.StudentDTO;
import org.acme.repository.StudentsRepo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StudentsService {
    @Inject
    private StudentsRepo studentsRepo;

    public void addStudents(Students student) {
        try {
            if (studentsRepo.findByEmail(student.getEmail()) == null) {                
                studentsRepo.persist(student);
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public void addStudentFromDTO(StudentDTO studentDTO) {
        try {
            if (studentsRepo.findByEmail(studentDTO.getEmail()) == null) {
                Students student = new Students();
                student.setName(studentDTO.getName());
                student.setAge(studentDTO.getAge());
                student.setEmail(studentDTO.getEmail());
                if (studentDTO.getUniversity() != null && !studentDTO.getUniversity().isEmpty()) {
                    List<University> managedUniversities = studentDTO.getUniversity().stream()
                        .map(u -> (University) University.findById(u.getId()))
                        .filter(u -> u != null)
                        .collect(Collectors.toList());
                    student.setUniversity(managedUniversities);
                }
                studentsRepo.persist(student);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating student: " + e.getMessage(), e);
        }
    }

    public void deleteStudents(Long id) {
        try {
            if(studentsRepo.findById((long)id) != null) {
                studentsRepo.deleteById(id);
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public List<Students> getAllStudents() {
        return studentsRepo.getAll();
    }

    public Students getStudentById(Long id) {
        return studentsRepo.findById(id);
    }
    
}
