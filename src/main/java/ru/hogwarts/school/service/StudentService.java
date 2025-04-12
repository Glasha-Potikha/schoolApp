package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {
    private Map<Long, Student> studentMap = new HashMap<>();
    private long lastId = 0;

    public Student addStudent(Student newStudent) {
        newStudent.setId(++lastId);
        studentMap.put(lastId, newStudent);
        return newStudent;
    }

    public Student getStudent(long id) {
        return studentMap.get(id);
    }

    public Student editStudent(Student updateStudent) {
        studentMap.put(updateStudent.getId(), updateStudent);
        return updateStudent;
    }

    public Student deleteStudent(long id) {
        return studentMap.remove(id);
    }

    public Collection<Student> filterForAge(int years) {
        Collection<Student> res = new LinkedList<>();
        Student s;
        Iterator<Student> iterator = studentMap.values().iterator();
        while (iterator.hasNext()) {
            s = iterator.next();
            if (s.getAge() == years) {
                res.add(s);
            }
        }
        return res;
    }
}
