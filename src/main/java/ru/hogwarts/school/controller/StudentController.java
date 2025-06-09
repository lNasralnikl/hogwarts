package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/student")
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);

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

    //Вывод студентов с именеами на определенную букву
    @GetMapping("/findAll/letter")
    public ResponseEntity<List<String>> getStudentNameStartWithLetter(@RequestParam("letter") String letter){

        if (letter.isEmpty() || letter == null){
            return ResponseEntity.badRequest().build();
        }

        List<String> names = studentService.getStudentNameStartWith(letter);
        return ResponseEntity.ok(names);
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

    //Выполнение шага 4 - сумма значений
    @GetMapping("/sum")
    public Boolean getSum(Long limit){

        //Первый способ
        logger.info("Запуск первого метода подсчета суммы");
        long startTime1 = System.currentTimeMillis();
        Long sum1 = LongStream
                .rangeClosed(1, limit)
                .parallel()
                .sum();

        long finishTime1 = System.currentTimeMillis() - startTime1;
        logger.info("Время работы 1 способа: " + finishTime1);

        //Второй способ
        logger.info("Запуск второго метода подсчета суммы");
        long startTime2 = System.currentTimeMillis();
        Long sum2 = limit/2 + limit*limit/2;
        long finishTime2 = System.currentTimeMillis() - startTime2;
        logger.info("Время работы 2 способа: " + finishTime2);

        //Третий способ
        logger.info("Запуск метода из задания подсчета суммы");
        long startTime3 = System.currentTimeMillis();
        long sum = Stream.iterate(1L, a -> a +1) .limit(1_000_000) .reduce(0L, (a, b) -> a + b );

        long finishTime3 = System.currentTimeMillis() - startTime3;
        logger.info("Время работы заданного способа: " + finishTime3);

        return sum == sum1 && sum2 == sum1;

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
