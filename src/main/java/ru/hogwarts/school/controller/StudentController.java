package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //Добавление студента
    @PostMapping
    public Student createStudent(@RequestBody Student student){
        if (student.getFaculty() != null) {
            student.setFaculty(null);
        }
        return studentService.addStudent(student);
    }

    //Вывод всех студентов
    @GetMapping("/allStudents")
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    //Удаление студента
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    //Изменение студента
    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student){
        Student updatedStudent = studentService.updateStudent(student);
        return updatedStudent != null
                ? ResponseEntity.ok(updatedStudent)
                : ResponseEntity.badRequest().build();
    }

    //Поиск по имени
    @GetMapping("/name")
    public List<Student> findByName(@RequestParam("name") String name){
        return studentService.findStudentByName(name);
    }

    //Поиск по возрасту
    @GetMapping
    public ResponseEntity<Collection<Student>> finByAge(@RequestParam(required = false) int age){
        if (age > 0){
            return ResponseEntity.ok(studentService.findStudentByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    //Поиск в диапазоне возрастов
    @GetMapping("/by-age-between")
    public List<Student> findByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge){
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    //Поиск по id
    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id){
        Student student = studentService.findById(id);
        if (student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    //Получение факультета по id
    @GetMapping("/{id}/faculty")
    public Faculty getFacultyByStudent(@PathVariable Long id){
        return studentService.findById(id).getFaculty();
    }

    //SQL запросы
    //Количество студентов
    @GetMapping("/count")
    public Long getAllStudentsQuantity(){
        return studentService.getAllStudentsQuantity();
    }

    @GetMapping("/avg-age")
    public double getAvgAgeOfAllStudents(){
        return studentService.getAvgAgeOfAllStudents();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents(){
        return studentService.getLastFiveStudents();
    }

    @PutMapping("add-students-for-test")
    void addStudentsForTest(){
        studentService.addStudentsForDB();
    }
}
