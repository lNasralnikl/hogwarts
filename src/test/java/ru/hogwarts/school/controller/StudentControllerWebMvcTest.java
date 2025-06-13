package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    //Добавление студента
    @Test
    void SaveStudentTest() throws Exception {
        Long id = 1L;
        String name = "Name";
        int age = 10;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.age").value(age));

    }

    //Вывод всех студентов
    @Test
    void printAllStudentsTest() throws Exception  {

        Long id = 1L;
        String name = "Name";
        int age = 10;

        Long id2 = 2L;
        String name2 = "Name2";
        int age2 = 110;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentService.getAllStudents()).thenReturn(List.of(student2, student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/allStudents")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    //Удаление студента
    @Test
    void deleteStudentTest() throws Exception {

        Long id = 1L;

        doNothing()
                .when(studentService)
                .deleteStudent(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id))
                .andExpect(status().isOk());
    }

    //Поиск по имени
    @Test
    void searchByName() throws Exception  {

        Long id = 1L;
        String name = "Name";
        int age = 10;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);


        when(studentService.findStudentByName(name)).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/name")
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));
    }

    //Поиск по возрасту
    @Test
    void searchByColor() throws Exception  {

        Long id = 1L;
        String name = "Name";
        int age = 10;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentService.findStudentByAge(age)).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .param("age", String.valueOf(age))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(age));
    }

    //Поиск студентов в диапазоне возрастов
    @Test
    void searchStudentsBetweenAge() throws Exception  {

        int minAge = 9;
        int maxAge = 25;

        Long id = 1L;
        String name = "Name";
        int age = 10;

        Long id2 = 2L;
        String name2 = "Name2";
        int age2 = 110;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentService.findByAgeBetween(minAge, maxAge)).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-age-between")
                        .param("minAge", String.valueOf(minAge))
                        .param("maxAge", String.valueOf(maxAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].age").value(age))
                .andExpect(jsonPath("$[0].name").value(name));
    }


    //Поиск по ID
    @Test
    void searchById() throws Exception  {

        Long id = 1L;
        String name = "Name";
        int age = 10;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentService.findById(id)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    //Изменение студента
    @Test
    void updateFaculty() throws Exception{

        Long id = 1L;
        String name = "Name";
        int age = 10;

        String name2 = "Name2";
        int age2 = 101;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student student2 = new Student();
        student2.setId(id);
        student2.setName(name2);
        student2.setAge(age2);

        when(studentService.updateStudent(any(Student.class))).thenReturn(student2);

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", id);
        requestBody.put("name", name);
        requestBody.put("age", age);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(requestBody.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.age").value(age2));

    }

    @Test
    //Параллельный вывод студентов
    void parallelPrintNames() throws Exception {

        //Создание студентов
        when(studentService.getAllStudents()).thenReturn(List.of(
                new Student(1L, "Name1", 10),
                new Student(2L, "Name2", 10),
                new Student(3L, "Name3", 10),
                new Student(4L, "Name4", 10),
                new Student(5L, "Name5", 10),
                new Student(6L, "Name6", 10)
        ));

        mockMvc.perform(get("/student/print-parallel"))
                .andExpect(status().isOk());

    }

    @Test
        //Параллельный вывод студентов
    void synchronizedPrintNames() throws Exception {

        //Создание студентов
        when(studentService.getAllStudents()).thenReturn(List.of(
                new Student(1L, "Name1", 10),
                new Student(2L, "Name2", 10),
                new Student(3L, "Name3", 10),
                new Student(4L, "Name4", 10),
                new Student(5L, "Name5", 10),
                new Student(6L, "Name6", 10)
        ));

        mockMvc.perform(get("/student/print-synchronized"))
                .andExpect(status().isOk());

    }

}

