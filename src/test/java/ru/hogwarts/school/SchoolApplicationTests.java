package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyControllerTest;
import ru.hogwarts.school.controller.StudentController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SchoolApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private StudentController studentController;

	@Autowired
	private FacultyControllerTest facultyController;

	@Test
	void contextLoads() {
		assertThat(studentController).isNotNull();
		assertThat(facultyController).isNotNull();
	}

}
