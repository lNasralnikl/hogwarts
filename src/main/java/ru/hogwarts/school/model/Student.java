package ru.hogwarts.school.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private  String name;
    private  int age;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    @JsonBackReference
    private Faculty faculty;

    //Геттеры и сеттеры по факультету
    public Faculty getFaculty(){
        return faculty;
    }

    public void setFaculty(Faculty faculty){
        this.faculty = faculty;
    }

    //Пустой конструктор для устранения ошибки в SWAGGER
    public Student(){}

    public Student(Long id, String name, int age) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Student student = (Student) object;
        return getAge() == student.getAge() && Objects.equals(getName(), student.getName()) && Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge(), getId());
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
