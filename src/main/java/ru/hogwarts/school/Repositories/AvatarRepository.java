package ru.hogwarts.school.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

Optional<Avatar> findByStudentId(Long studentId);

}
