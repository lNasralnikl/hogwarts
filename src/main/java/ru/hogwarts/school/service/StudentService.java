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
        return studentRepository.save(student);
    }

    //Поиск по id
    public Student findById(Long id) {
        if (studentRepository.findById(id) != null){
        return studentRepository.findById(id).get();}
        throw new BadRequest("Такого студента нет!");
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

}
