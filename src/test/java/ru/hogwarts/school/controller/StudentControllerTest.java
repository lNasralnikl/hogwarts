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

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/student";
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    //Добавление студента
    @Test
    void createStudent_returnNewStudent() {

        Student student = new Student();
        student.setAge(10);
        student.setName("Name");

        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl,
                student,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(student.getName());
        assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
    }

    //Вывод всех студентов
    @Test
    void whenGetAllStudents_returnListOfStudents() {

        Student student1 = new Student();
        student1.setName("Name1");
        student1.setAge(11);
        restTemplate.postForEntity(
                baseUrl,
                student1,
                Student.class
        );
        Student student2 = new Student();
        student2.setName("Name2");
        student2.setAge(22);
        restTemplate.postForEntity(
                baseUrl,
                student2,
                Student.class
        );

        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/allStudents",
                List.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(2);
        assertThat(response.getBody()).isNotNull();
    }

    //Удаление студента
    @Test
    void whenDeleteStudent_thenRemoveStudentFromDB() {

        Student student = new Student();
        student.setAge(10);
        student.setName("Name");

        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl,
                student,
                Student.class
        );

        Long id = response.getBody().getId();

        restTemplate.delete(baseUrl + "/" + id);

        ResponseEntity<Student> response1 = restTemplate.getForEntity(
                baseUrl + "/" + id,
                Student.class
        );

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //Поиск по имени
    @Test
    void whenSearchByName_returnStudentsIsEqualName() {
        String name = "Name";

        Student student = new Student();
        student.setAge(10);
        student.setName("Name");

        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);

        Student student1 = new Student();
        student1.setAge(10);
        student1.setName("Not");

        ResponseEntity<Student> response1 = restTemplate.postForEntity(baseUrl, student1, Student.class);


        ResponseEntity<List<Student>> response2 = restTemplate.exchange(
                baseUrl + "/name?name=" + name,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody().size()).isEqualTo(1);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().get(0).getName()).isEqualTo(name);
    }

    //Поиск по возрасту
    @Test
    void whenSearchByAge_returnStudentsIsEqualAge() {
        // Создаем тестовых студентов
        Student student1 = new Student(null, "Name", 20);
        Student student2 = new Student(null, "No name", 20);
        restTemplate.postForEntity(baseUrl, student1, Student.class);
        restTemplate.postForEntity(baseUrl, student2, Student.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                baseUrl + "?age=20",
                Collection.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2);
    }

    //Поиск в диапазоне возрастов
    @Test
    void findByAgeBetween_ShouldReturnStudentsInAgeRange() {

        studentRepository.deleteAll();

        // Создаем тестовых студентов
        Student student1 = new Student(null, "Range Test 1", 20);
        Student student2 = new Student(null, "Range Test 2", 25);
        Student student3 = new Student(null, "Range Test 3", 30);
        restTemplate.postForEntity(baseUrl, student1, Student.class);
        restTemplate.postForEntity(baseUrl, student2, Student.class);
        restTemplate.postForEntity(baseUrl, student3, Student.class);

        List<Student> students = restTemplate.getForObject(
                baseUrl + "/by-age-between?minAge=20&maxAge=25",
                List.class);

        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(2);
    }

    //Поиск по ID
    @Test
    void whenSearchStudentById_thenReturnStudentWithEqualId() {
        Student student = new Student();
        student.setAge(16);
        student.setName("Name");
        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);

        Long id = response.getBody().getId();

        ResponseEntity<Student> response1 = restTemplate.exchange(
                baseUrl + "/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Student>() {
                }
        );

        assertThat(response1.getBody().getId()).isEqualTo(id);
        assertThat(response1.getBody()).isNotNull();
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    //Изменение студента - при проведении теста выдает возраст равный нулю
    @Test
    void whenUpdateStudent_thenChangeStudentDataInDB() {
        //Создаем студента
        Student student = new Student();
        student.setAge(10);
        student.setName("Jonny");
        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);
        Long id = response.getBody().getId();

        //Обновляем данные
        Student updatedStudent = new Student();
        updatedStudent.setName("Jonny P'didi");
        updatedStudent.setAge(20);
        updatedStudent.setId(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> requestEntity = new HttpEntity<>(updatedStudent, headers);

        ResponseEntity<Student> response1 = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                requestEntity,
                Student.class
        );

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response1.getBody()).isNotNull();
        assertThat(response1.getBody().getAge()).isEqualTo(20);
        assertThat(response1.getBody().getName()).isEqualTo("Jonny P'didi");
    }

    //Поиск факультета по ID
    @Test
    void whenSearchFacultyById_thenReturnCorrectFaculty() {

        Faculty faculty = new Faculty();
        faculty.setName("Чудаки");
        faculty.setColor("Черный");

        facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Ноксвелл");
        student.setAge(19);
        student.setFaculty(faculty);

        student = studentRepository.save(student);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl + "/" + student.getId() + "/faculty",
                HttpMethod.GET,
                null,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        assertThat(response.getBody().getId()).isEqualTo(faculty.getId());
        assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());

    }

}
