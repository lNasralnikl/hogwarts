package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.Repositories.StudentRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FacultyControllerTest {


    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/faculty";
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    //Добавление факультета
    @Test
    void whenCreateFaculty_returnNewFaculty() {

        Faculty faculty = new Faculty();
        faculty.setColor("Random");
        faculty.setName("Name");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl,
                faculty,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());
    }

    //Вывод всех факультетов
    @Test
    void whenGetAllFaculties_returnListOfFaculties() {

        Faculty faculty1 = new Faculty();
        faculty1.setName("Name1");
        faculty1.setColor("Random1");
        restTemplate.postForEntity(
                baseUrl,
                faculty1,
                Faculty.class
        );
        Faculty faculty2 = new Faculty();
        faculty2.setName("Name2");
        faculty2.setColor("Random2");
        restTemplate.postForEntity(
                baseUrl,
                faculty2,
                Faculty.class
        );

        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/allFaculties",
                List.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(2);
        assertThat(response.getBody()).isNotNull();
    }

    //Удаление студента
    @Test
    void whenDeleteFaculty_thenRemoveFacultyFromDB() {

        Faculty faculty = new Faculty();
        faculty.setColor("Random");
        faculty.setName("Name");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl,
                faculty,
                Faculty.class
        );

        Long id = response.getBody().getId();

        restTemplate.delete(baseUrl + "/" + id);

        ResponseEntity<Faculty> response1 = restTemplate.getForEntity(
                baseUrl + "/" + id,
                Faculty.class
        );

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //Поиск по имени
    @Test
    void whenSearchByName_returnFacultiesIsEqualName() {
        String name = "Name";

        Faculty faculty = new Faculty();
        faculty.setColor("Random1");
        faculty.setName("Name");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        Faculty faculty1 = new Faculty();
        faculty1.setColor("Random1");
        faculty1.setName("Not");

        ResponseEntity<Faculty> response1 = restTemplate.postForEntity(baseUrl, faculty1, Faculty.class);


        ResponseEntity<List<Faculty>> response2 = restTemplate.exchange(
                baseUrl + "/name?name=" + name,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody().size()).isEqualTo(1);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().get(0).getName()).isEqualTo(name);
    }

    //Поиск по цвету
    @Test
    void whenSearchByColor_returnFacultiesIsEqualColor() {
        String color = "Red";

        Faculty faculty = new Faculty();
        faculty.setColor("Red");
        faculty.setName("Name");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        Faculty faculty1 = new Faculty();
        faculty1.setColor("Random1");
        faculty1.setName("Not");

        ResponseEntity<Faculty> response1 = restTemplate.postForEntity(baseUrl, faculty1, Faculty.class);


        ResponseEntity<List<Faculty>> response2 = restTemplate.exchange(
                baseUrl + "/color?color=" + color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody().size()).isEqualTo(1);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().get(0).getColor()).isEqualTo(color);
    }

    //Поиск по цвету
    @Test
    void whenSearchByColorOrName_returnFacultiesIsEqualColorOrName() {

        Faculty faculty = new Faculty();
        faculty.setColor("Red");
        faculty.setName("Name");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        Faculty faculty1 = new Faculty();
        faculty1.setColor("Random1");
        faculty1.setName("Not");

        ResponseEntity<Faculty> response1 = restTemplate.postForEntity(baseUrl, faculty1, Faculty.class);


        ResponseEntity<List<Faculty>> response2 = restTemplate.exchange(
                baseUrl + "/search?searchable=Name",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody().size()).isEqualTo(1);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().get(0).getName()).isEqualTo("Name");
    }

    //Поиск по ID
    @Test
    void whenSearchFacultyById_thenReturnFacultyWithEqualId() {
        Faculty faculty = new Faculty();
        faculty.setColor("16");
        faculty.setName("Name");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        Long id = response.getBody().getId();

        ResponseEntity<Faculty> response1 = restTemplate.exchange(
                baseUrl + "/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Faculty>() {
                }
        );

        assertThat(response1.getBody().getId()).isEqualTo(id);
        assertThat(response1.getBody()).isNotNull();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    //Изменение факультета
    @Test
    void whenUpdateFaculty_thenChangeFacultyDataInDB() {
        //Создаем факультет
        Faculty faculty = new Faculty();
        faculty.setColor("10");
        faculty.setName("Jonny");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);
        Long id = response.getBody().getId();

        //Обновляем данные
        Faculty updatedfaculty = new Faculty();
        updatedfaculty.setName("Jonny P'didi");
        updatedfaculty.setColor("20");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(updatedfaculty, headers);

        ResponseEntity<Faculty> response1 = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                requestEntity,
                Faculty.class
        );

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response1.getBody()).isNotNull();
        assertThat(response1.getBody().getColor()).isEqualTo("20");
        assertThat(response1.getBody().getName()).isEqualTo("Jonny P'didi");
    }

    //Вывод студентов факультета
    @Test
    void whenSearchStudentsFromFaculty_thenReturnListOfStudents() {
        Faculty faculty = new Faculty();
        faculty.setName("Name");
        faculty.setColor("Прозрачный");

        faculty = facultyRepository.save(faculty);

        Student student1 = new Student();
        student1.setName("Первый");
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setName("Второй");
        student2.setFaculty(faculty);

        studentRepository.save(student1);
        studentRepository.save(student2);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/" + faculty.getId() + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }

}

    

