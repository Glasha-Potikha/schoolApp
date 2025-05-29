package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyService facultyService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testGetFacultyInfo_found() throws Exception {
        Faculty faculty = new Faculty(1L, "медный", "Тестор");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Тестор"));
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Long id = 1L;
        String name = "Тестор";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", "медный");

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor("медный");

        when(facultyRepository.save(Mockito.any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name));

    }

    @Test
    public void testEditFaculty_success() throws Exception {
        Faculty request = new Faculty(1L, "медный", "Тестор");
        Faculty updated = new Faculty(1L, "золотой", "Тестор");
        Mockito.when(facultyService.editFaculty(Mockito.any(Faculty.class))).thenReturn(updated);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("золотой"));
    }

    @Test
    public void testEditFaculty_notFound() throws Exception {
        Faculty request = new Faculty(1L, "медный", "Нектон");
        Mockito.when(facultyService.editFaculty(Mockito.any(Faculty.class))).thenReturn(null);
        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        Faculty deleted = new Faculty(1L, "медный", "Удаляемый");
        Mockito.when(facultyRepository.findById(1L)).thenReturn(Optional.of(deleted));
        Mockito.doNothing().when(facultyRepository).deleteById(1L);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Удаляемый"));
    }

    @Test
    public void testFilterForColor() throws Exception {
        Faculty f1 = new Faculty(1L, "медный", "Тестор");
        Faculty f2 = new Faculty(2L, "медный", "Тестур");
        Mockito.when(facultyService.filterForColor("медный")).thenReturn(List.of(f1, f2));

        mockMvc.perform(get("/faculty/filter/color").param("color", "медный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testFilterForName() throws Exception {
        Faculty f1 = new Faculty(1L, "медный", "Тестор");
        Mockito.when(facultyService.filterForName("Тестор")).thenReturn(List.of(f1));

        mockMvc.perform(get("/faculty/filter/name").param("name", "Тестор"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetFacultyStudents() throws Exception {
        Faculty f1 = new Faculty(1L, "медный", "Тестор");
        Student s1 = new Student(10, "Гарри", 1L);
        Student s2 = new Student(13, "Рон", 2L);
        s1.setFaculty(f1);
        s2.setFaculty(f1);
        Mockito.when(facultyRepository.findById(1L)).thenReturn(Optional.of(f1));
        Mockito.when(facultyService.getFacultyStudents(1L)).thenReturn(List.of(s1, s2));
        mockMvc.perform(get("/faculty/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
