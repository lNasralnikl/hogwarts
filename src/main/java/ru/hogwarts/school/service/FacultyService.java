package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.Errors.BadRequest;
import ru.hogwarts.school.Repositories.FacultyRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    //Добавление
    public Faculty addFaculty(Faculty faculty){
        logger.info("Вызов метода создания факультета " + faculty);
        if (faculty.getId() != null){
            logger.info("Ошибка при создании факультета с указанием ID");
            throw new BadRequest("Для добавления нового факультета указать нулевой ID!");
        }
        return facultyRepository.save(faculty);
    }

    //Обновление факультета
    public Faculty updateFaculty(Faculty faculty){
        logger.info("Вызов метода обновления факультета " + faculty);
        if(faculty.getId() == null){
            logger.info("Ошибка при обновлении факультета без указания ID");
            throw new BadRequest("Для изменения факультета введите не нулевой ID!");
        }
        return facultyRepository.save(faculty);
    }

    //Поиск по id
    public Faculty findById(Long id) {
        logger.info("Вызов метода поиска факультета по ID " +id);
        if (!facultyRepository.existsById(id)){
            logger.info("Ошибка при поиске факультета по ID " + id);
            throw new BadRequest("Такого факультета нет!");
        }
        return facultyRepository.findById(id).get();
    }

    //Удаление факультета
    public void deleteFaculty(Long id) {
        logger.info("Вызов метода удаления факультета по ID" + id);
        if (facultyRepository.findById(id) == null) {
            logger.info("Ошибка при попытке удаления несуществующего факультета c ID" + id);
            throw new BadRequest("Такого факультета нет");
        }
        facultyRepository.deleteById(id);
    }

    //Вывод всех факультетов
    public List<Faculty> getAllFaculties() {
        logger.info("Вызов метода вывода всех факультетов");
        return facultyRepository.findAll();
    }

    //Поиск по цвету
    public List<Faculty> findFacultyByColor(String color){
        logger.info("Вызов метода поиска факультета по цвету: " + color);
        return facultyRepository.findByColor(color);
    }

    //Поиск по названию
    public List<Faculty> findFacultyByName(String name){
        logger.info("Вызов метода поиска факультета по названию: " + name);
        return facultyRepository.findFacultyByName(name);
    }

    //Поиск по названию или цвету
    public List<Faculty> findByNameOrColorIgnoreCase(String searchable) {
        logger.info("Вызов метода поиска факультета по названию или цвету: " + searchable);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(searchable, searchable);
    }

}
