package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student newStudent) {
        return studentRepository.save(newStudent);
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).get();//studentMap.get(id)
    }

    public Student editStudent(Student updateStudent) {
        return studentRepository.save(updateStudent);
    }

    public Student deleteStudent(long id) {
        Student student = studentRepository.findById(id).get();
        studentRepository.deleteById(id);
        return student;
    }

    public Collection<Student> filterForAge(int years) {
        return studentRepository.findByAge(years);
    }

}
