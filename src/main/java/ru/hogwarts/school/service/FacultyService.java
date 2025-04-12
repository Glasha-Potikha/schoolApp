package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class FacultyService {
    private Map<Long, Faculty> facultyMap = new HashMap<>();
    private long lastId = 0;

    public Faculty addFaculty(Faculty newFaculty) {
        newFaculty.setId(++lastId);
        return facultyMap.put(lastId, newFaculty);
    }

    public Faculty getFaculty(long id) {
        return facultyMap.get(id);
    }

    public Faculty editFaculty(Faculty updateFaculty) {
        return facultyMap.put(updateFaculty.getId(), updateFaculty);
    }

    public Faculty deleteFaculty(long id) {
        return facultyMap.remove(id);
    }

    public Collection<Faculty> filterForColor(String color) {
        Collection<Faculty> res = new LinkedList<>();
        Faculty f;
        Iterator<Faculty> iterator = facultyMap.values().iterator();
        while (iterator.hasNext()) {
            f = iterator.next();
            if (f.getColor().equals(color)) {
                res.add(f);
            }
        }
        return res;
    }
}
