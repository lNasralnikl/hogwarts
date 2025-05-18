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
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    //Добавление факультета
    @Test
    void SaveFacultyTest() throws Exception {
        Long id = 1L;
        String name = "Name";
        String color = "Color";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/faculty")
                .content(facultyObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(color));

    }

    //Вывод всех факультетов
    @Test
    void printAllFacultiesTest() throws Exception  {

        Long id = 1L;
        String name = "Name";
        String color = "Color";

        Long id2 = 2L;
        String name2 = "Name2";
        String color2 = "Color2";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty, faculty2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/allFaculties")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    //Удаление факультета
    @Test
    void deleteFacultyTest() throws Exception {

        Long id = 1L;

        doNothing()
                .when(facultyService)
                .deleteFaculty(id);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/faculty/{id}", id))
                .andExpect(status().isOk());
    }

    //Поиск по имени
    @Test
    void searchByName() throws Exception  {

        Long id = 1L;
        String name = "Name";
        String color = "Color";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);


        when(facultyService.findFacultyByName(name)).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/name")
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));
    }

    //Поиск по цвету
    @Test
    void searchByColor() throws Exception  {

        Long id = 1L;
        String name = "Name";
        String color = "Color";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.findFacultyByColor(color)).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color")
                        .param("color", color)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value(color));
    }

    //Поиск по цвету или наименованию
    @Test
    void searchByNameOrColor() throws Exception  {

        Long id = 1L;
        String name = "Name";
        String color = "Color";

        String search = "Name";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.findByNameOrColorIgnoreCase(search)).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/search")
                        .param("searchable", search)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value(color))
                .andExpect(jsonPath("$[0].name").value(name));
    }


    //Поиск по ID
    @Test
    void searchById() throws Exception  {

        Long id = 1L;
        String name = "Name";
        String color = "Color";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.findById(id)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    //Изменение факультета
    @Test
    void updateFaculty() throws Exception{

        Long id = 1L;
        String name = "Name";
        String color = "Color";

        String name2 = "Name2";
        String color2 = "Color2";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty2);

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", id);
        requestBody.put("name", name);
        requestBody.put("color", color);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(requestBody.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.color").value(color2));

    }

    //Вывод всех студентов факультета
    @Test
    void printAllStudentsOfFacultyTest() throws Exception  {

        Long id = 1L;
        String name = "Name";
        String color = "Color";

        Long studentId = 2L;
        String studentName = "Name2";
        int age = 10;

        Faculty faculty = mock(Faculty.class);
        when(faculty.getId()).thenReturn(id);
        when(faculty.getName()).thenReturn(name);
        when(faculty.getColor()).thenReturn(color);

        Student student = new Student();
        student.setId(studentId);
        student.setName(studentName);
        student.setAge(age);
        student.setFaculty(faculty);

        List<Student> students = new ArrayList<>();
        students.add(student);

        when(facultyService.findById(id)).thenReturn(faculty);
        when(faculty.getStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}/students", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

}
