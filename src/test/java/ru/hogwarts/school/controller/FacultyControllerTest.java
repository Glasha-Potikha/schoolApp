package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;


    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private FacultyService facultyService;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testGetFacultyInfo() throws Exception {
        Faculty mockFaculty = new Faculty();
        mockFaculty.setId(1L);
        mockFaculty.setName("Тестор");
        Mockito.when(facultyService.getFaculty(1)).thenReturn(mockFaculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/1", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Тестор");
    }

    @Test
    public void testFilterForColor() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1l);
        faculty.setName("Тестор1");
        faculty.setColor("золотой");
        Faculty faculty2 = new Faculty();
        faculty2.setId(2l);
        faculty2.setName("Тестур2");
        faculty2.setColor("золотой");
        Collection<Faculty> mockFaculties = List.of(faculty, faculty2);
        Mockito.when(facultyService.filterForColor("золотой")).thenReturn(mockFaculties);
        ResponseEntity<List<Object>> response = restTemplate.exchange("http://localhost:" + port + "/faculty/filter/color?color=золотой",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Object>>() {
                }
        );
        ObjectMapper mapper = new ObjectMapper();
        List<Faculty> faculties = response.getBody().stream()
                .map(obj -> mapper.convertValue(obj, Faculty.class))
                .collect(Collectors.toList());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(faculties).hasSize(2);
        assertThat(faculties).extracting(Faculty::getName)
                .contains("Тестор1", "Тестур2");
    }

    @Test
    public void testFilterForName() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1l);
        faculty.setName("Тестор1");
        faculty.setColor("золотой");

        Collection<Faculty> mockFaculties = List.of(faculty);
        Mockito.when(facultyService.filterForName("Тестор1")).thenReturn(mockFaculties);
        ResponseEntity<List<Faculty>> response = restTemplate.exchange("http://localhost:" + port + "/faculty/filter/name?name=Тестор1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );
        ObjectMapper mapper = new ObjectMapper();
        List<Faculty> faculties = response.getBody().stream()
                .map(obj -> mapper.convertValue(obj, Faculty.class))
                .collect(Collectors.toList());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(faculties).hasSize(1);
        assertThat(faculties).extracting(Faculty::getName)
                .contains("Тестор1");
    }

    @Test
    public void testGetFacultyStudents() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1l);
        faculty.setName("Тестор1");

        Student student = new Student();
        student.setId(1L);
        student.setName("Один Тестович");
        student.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFaculty(faculty);
        student2.setName("Два Тестовна");

        Student student3 = new Student();
        student3.setId(3L);
        student3.setFaculty(faculty);
        student3.setName("Три Тестовна");

        List<Student> mockStudents = List.of(student, student2, student3);
        Mockito.when(facultyService.getFacultyStudents(1L)).thenReturn(mockStudents);
        ResponseEntity<List<Object>> response = restTemplate.exchange("http://localhost:" + port + "/faculty/students/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Object>>() {
                }
        );

        ObjectMapper mapper = new ObjectMapper();
        List<Student> students = response.getBody().stream()
                .map(obj -> mapper.convertValue(obj, Student.class))
                .collect(Collectors.toList());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(students).hasSize(3);
        assertThat(students).extracting(Student::getName)
                .contains("Один Тестович", "Два Тестовна", "Три Тестовна");
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Тестор1");
        faculty.setColor("золотой");
        Mockito.when(facultyService.addFaculty(Mockito.any(Faculty.class)))
                .thenReturn(faculty);

        Faculty response = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Тестор1");
    }

    @Test
    public void testEditFaculty() throws Exception {
        //данное для запроса:
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Тестор1");
        faculty.setColor("золотой");
        //возвращаемое из сервиса
        Faculty editedFaculty = new Faculty();
        editedFaculty.setId(1L);
        editedFaculty.setName("Тестор1");
        editedFaculty.setColor("медный");

        Mockito.when(facultyService.editFaculty(Mockito.any(Faculty.class)))
                .thenReturn(editedFaculty);

        HttpEntity<Faculty> entity = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                entity,
                Faculty.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("медный");
    }

    @Test
    public void testEditFaculty_notFound() {
        Faculty faculty = new Faculty();
        faculty.setId(9L);
        faculty.setName("Неведомый");

        // Сервис возвращает null
        Mockito.when(facultyService.editFaculty(Mockito.any(Faculty.class)))
                .thenReturn(null);

        HttpEntity<Faculty> entity = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                entity,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Удаляемый");

        Mockito.when(facultyService.deleteFaculty(1L)).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/1",
                HttpMethod.DELETE,
                null,
                Faculty.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Удаляемый");
    }
}


