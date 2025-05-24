package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Errors.BadRequest;
import ru.hogwarts.school.Repositories.StudentRepository;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //Добавление
    public Student addStudent(Student student) {
        if (student.getId() != null) {
            throw new BadRequest("Для добавления нового студента укажите нулевой ID!");
        }
        return studentRepository.save(student);
    }

    //Обновление
    public Student updateStudent(Student student) {
        if (student.getId() == null) {
            throw new BadRequest("Для обновления студента укажите не нулевой ID!");
        }
        return studentRepository.save(student);
    }

    //Поиск по id
    public Student findById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new BadRequest("Такого студента нет!");
        }
        return studentRepository.findById(id).get();
    }

    //Удаление студента
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);

    }

    //Вывод всех студентов
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    //Поиск по имени
    public List<Student> findStudentByName(String name) {
        return studentRepository.findByName(name);
    }

    //Поиск по возрасту
    public List<Student> findStudentByAge(int age) {
        return studentRepository.findByAge(age);
    }

    //Поиск в диапазоне возрастов
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    //SQL запросы
    public Long getAllStudentsQuantity() {
        return studentRepository.getAllStudentsQuantity();
    }

    public double getAvgAgeOfAllStudents() {
        return studentRepository.getAvgAgeOfAllStudents();
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents();
    }

    //

    //Заполнение базы
    public void addStudentsForDB() {

        studentRepository.deleteByNameStartingWith("Name");

        for (int i = 0; i < 10; i++) {

            Student student = new Student();
            student.setAge(i + 10);
            student.setName("Name" + i);
            if (findStudentByName(student.getName()).isEmpty()) {
                studentRepository.save(student);
            }
        }
    }

}
