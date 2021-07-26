package com.quizapp.com.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.quizapp.com.domain.Student;
import com.quizapp.com.repository.StudentRepository;

@DataJpaTest
public class StudentRepositoryTest {

	@Autowired
	private StudentRepository underTest;

	@AfterEach
	void tearDown() {

		underTest.deleteAll();

	}

	@Test
	@DisplayName("Just to check that find by id works")
	void itShouldCheckWhenAStudentDoesNotExists() {
		// What we have
		Long id = 0L;

		//When
		Optional<Student> expected = underTest.findById(id);

		//Then
		assertThat(expected.isPresent()).isFalse();
	}
}
