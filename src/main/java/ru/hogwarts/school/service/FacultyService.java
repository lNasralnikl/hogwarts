package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Errors.BadRequest;
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //Добавление
    public Faculty addFaculty(Faculty faculty){
        if (faculty.getId() != null){
            throw new BadRequest("Для добавления нового факультета указать нулевой ID!");
        }
        return facultyRepository.save(faculty);
    }

    //Обновление факультета
    public Faculty updateFaculty(Faculty faculty){
        if(faculty.getId() == null){
            throw new BadRequest("Для изменения факультета введите не нулевой ID!");
        }
        return facultyRepository.save(faculty);
    }

    //Поиск по id
    public Faculty findById(Long id) {
        if (!facultyRepository.existsById(id)){
            throw new BadRequest("Такого факультета нет!");
        }
        return facultyRepository.findById(id).get();
    }

    //Удаление факультета
    public void deleteFaculty(Long id) {
        if (facultyRepository.findById(id) == null) {
            throw new BadRequest("Такого факультета нет");
        }
        facultyRepository.deleteById(id);
    }

    //Вывод всех факультетов
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    //Поиск по цвету
    public List<Faculty> findFacultyByColor(String color){
        return facultyRepository.findByColor(color);
    }

    //Поиск по названию
    public List<Faculty> findFacultyByName(String name){
        return facultyRepository.findFacultyByName(name);
    }

    //Поиск по названию или цвету
    public List<Faculty> findByNameOrColorIgnoreCase(String searchable) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchable, searchable);
    }
/*
    //Поиск по цвету

    public Collection<Faculty> findByColor(String color) {
        List<Faculty> foundedFaculties = faculties.values().stream()
                .filter(f -> f.getColor() == color)
                .toList();
        if (foundedFaculties.isEmpty()) {
            throw new BadRequest("Факультеты не найдены");
        }
        return foundedFaculties;
    }


    //Поиск по имени
    public List<Faculty> findByName(String name) {
        List<Faculty> foundedFaculties = faculties.values().stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .toList();
        if (foundedFaculties.isEmpty()) {
            throw new BadRequest("Факультеты не найдены");
        }
        return foundedFaculties;

    }

     */

}
