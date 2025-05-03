package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    //Добавление факультета
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty){
        return facultyService.addFaculty(faculty);
    }

    //Вывод всех факультетов
    @GetMapping("/allFaculties")
    public List<Faculty> getAllFaculties(){
        return facultyService.getAllFaculties();
    }

    //Удаление факультета
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id){
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    //Изменение факультета
    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty){
        Faculty updatedFaculty = facultyService.addFaculty(faculty);

        if (updatedFaculty == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    //Поиск по имени
    @GetMapping("/name")
    public List<Faculty> findByName(@RequestParam("name") String name){
        return facultyService.findFacultyByName(name);
    }

    //Поиск по цвету
    @GetMapping
    public ResponseEntity<Collection<Faculty>> findByColor(@RequestParam(value = "color") String color){
    if (color != null && !color.isBlank()){
        return ResponseEntity.ok(facultyService.findFacultyByColor(color));
    }
    return ResponseEntity.ok(Collections.emptyList());

    }

    //Поиск по названию или цвету
    @GetMapping("/search")
    public List<Faculty> findByNameOrColorIgnoreCase(@RequestParam String searchable) {
        return facultyService.findByNameOrColorIgnoreCase(searchable);
    }

    //Поиск по id
    @GetMapping("{id}")
    public ResponseEntity<Faculty> findById(@PathVariable Long id){
        Faculty faculty = facultyService.findById(id);
        if (faculty == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    //Получение студентов факультета
    @GetMapping("/{id}/students")
    public List<Student> getStudentsByFaculty(@PathVariable Long id){
        return facultyService.findById(id).getStudents();
    }

}
