package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.Errors.BadRequest;
import ru.hogwarts.school.Repositories.StudentRepository;
import ru.hogwarts.school.model.Student;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //Добавление
    public Student addStudent(Student student) {

        logger.info("Вызов метода создания студента " + student);
        if (student.getId() != null) {
            logger.info("Ошибка при создании студента с указанием ID");

            throw new BadRequest("Для добавления нового студента укажите нулевой ID!");
        }
        return studentRepository.save(student);
    }

    //Обновление
    public Student updateStudent(Student student) {

        logger.info("Вызов метода обновления студента " + student);
        if (student.getId() == null) {
            logger.info("Ошибка при обновлении студента без указания ID");

            throw new BadRequest("Для обновления студента укажите не нулевой ID!");
        }
        return studentRepository.save(student);
    }

    //Поиск по id
    public Student findById(Long id) {

        logger.info("Вызов метода поиска студента по ID " + id);
        if (!studentRepository.existsById(id)) {
            logger.info("Ошибка при поиске несуществующего студента c ID " + id);

            throw new BadRequest("Такого студента нет!");
        }
        return studentRepository.findById(id).get();
    }

    //Удаление студента
    public void deleteStudent(Long id) {
        logger.info("Вызов метода удаления студента c ID " + id);
        if(studentRepository.findById(id) == null){
            logger.info("Ошибка при попытке удаления несуществующего студента c ID " + id);
            throw new BadRequest("Студента с ID " + id + " нет");
        }
        studentRepository.deleteById(id);

    }

    //Вывод всех студентов
    public List<Student> getAllStudents() {
        logger.info("Вызов метода вывода всех студентов");
        return studentRepository.findAll();
    }

    //Поиск по имени
    public List<Student> findStudentByName(String name) {
        logger.info("Вызов метода поиска студента по имени: " + name);
        return studentRepository.findByName(name);
    }

    //Поиск по возрасту
    public List<Student> findStudentByAge(int age) {
        logger.info("Вызов метода поиска студента по возрасту: " + age);
        return studentRepository.findByAge(age);
    }

    //Поиск в диапазоне возрастов
    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Вызов метода поиска студентов в диапазоне возрастов в диапазоне " + minAge + " : " + maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    //SQL запросы
    public Long getAllStudentsQuantity() {
        logger.info("Вызов метода вывода количества студентов");
        return studentRepository.getAllStudentsQuantity();
    }

    public double getAvgAgeOfAllStudents() {
        logger.info("Вызов метода вывода среднего возраста студентов");
        return studentRepository.getAvgAgeOfAllStudents();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Вызов метода вывода пятерых последних студентов");
        return studentRepository.getLastFiveStudents();
    }

    //Вывод студентов с именеами на определенную букву
    public List<String> getStudentNameStartWith(String letter){
        logger.info("Вызов метода вывода студентов с буквой " + letter.toUpperCase());
        return studentRepository.findAll()
                .parallelStream()
                .map(Student::getName)
                .filter(name -> name !=  null && name.toUpperCase().startsWith(letter.toUpperCase()))
                .sorted()
                .toList();
    }

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

    //Вывод имен студентов в нескольких потоках
    public void getSixNamesOfStudents(){

        List<Student> allStudents = getAllStudents();

        //Первые два имени
        System.out.println(allStudents.get(0).getName());
        System.out.println(allStudents.get(1).getName());

        //Второй пак имен
        new Thread(() -> {
            System.out.println(allStudents.get(2).getName());
            System.out.println(allStudents.get(3).getName());
        }).start();

        //Третий пак имен
        new Thread(() -> {
            System.out.println(allStudents.get(4).getName());
            System.out.println(allStudents.get(5).getName());
        }).start();

    }

    //Печать имен студентов в синхронизированном режиме
    public synchronized void printSynchronized(Student student){
        System.out.println(student.getName());
    }

    //Вывод синхронизированных имен студентов
    public void printSynchronizedNames(){

        List<Student> students = getAllStudents();

        //Первые два имени
        printSynchronized(students.get(0));
        printSynchronized(students.get(1));

        //Второй пак имен
        new Thread(() -> {
            printSynchronized(students.get(2));
            printSynchronized(students.get(3));
        }).start();

        //Третий пак имен
        new Thread(() -> {
            printSynchronized(students.get(4));
            printSynchronized(students.get(5));
        }).start();

    }
}

