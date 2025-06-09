package ru.hogwarts.school.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByName(String name);
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int minAge, int maxAge);


    @Query(value = "SELECT COUNT(*) FROM Student", nativeQuery = true)
    Long getAllStudentsQuantity();


    @Query(value = "SELECT AVG(age) FROM Student", nativeQuery = true)
    double getAvgAgeOfAllStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudents();

    void deleteByNameStartingWith(String prefix);
}
