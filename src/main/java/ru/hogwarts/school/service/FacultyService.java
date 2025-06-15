package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Transactional
    public Faculty addFaculty(Faculty newFaculty) {
        logger.info("Was invoked method for add Faculty");
        return facultyRepository.save(newFaculty);
    }

    @Transactional
    public Faculty getFaculty(long id) {
        logger.info("Was invoked method for get Faculty");
        return facultyRepository.findById(id).get();
    }

    @Transactional
    public Faculty editFaculty(Faculty updateFaculty) {
        logger.info("Was invoked method for edit Faculty");
        return facultyRepository.save(updateFaculty);
    }

    @Transactional
    public Faculty deleteFaculty(long id) {
        Faculty faculty = facultyRepository.findById(id).get();
        facultyRepository.deleteById(id);
        logger.info("Was invoked method for delete Faculty");
        return faculty;
    }

    @Transactional
    public Collection<Faculty> filterForColor(String color) {
        logger.info("Was invoked method for filter For Color");
        return facultyRepository.findByColorIgnoreCase(color);
    }

    @Transactional
    public Collection<Faculty> filterForName(String name) {
        logger.info("Was invoked method for filter For Name");
        return facultyRepository.findByNameIgnoreCase(name);
    }

    @Transactional
    public List<Student> getFacultyStudents(long id) {
        logger.info("Was invoked method for get Faculty Students");
        return getFaculty(id).getStudents();
    }
}

