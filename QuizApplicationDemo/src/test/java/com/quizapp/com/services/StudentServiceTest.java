package com.quizapp.com.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quizapp.com.domain.Student;
import com.quizapp.com.mapper.StudentMapperImpl;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.repository.StudentRepository;
import com.quizapp.com.services.implementation.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository;

	@InjectMocks
	private StudentMapperImpl studentMapper;

	private StudentService underTest;

	@InjectMocks
	private Student student;

	@BeforeEach
	void setUp() {
		underTest = new StudentServiceImpl(studentRepository, studentMapper);
		student.setId(1L);
		student.setName("Aditya Shukla");
		student.setEducation("Btech");

	}

	@DisplayName("It checks whether all students can be fetched")
	@Test
	void itCanGetAllStudent() {

		List<Student> expected = new ArrayList<>();
		expected.add(student);

		when(studentRepository.findAll()).thenReturn(expected);

		Set<StudentDTO> actual = underTest.getAllStudents();

		verify(studentRepository).findAll();

		assertThat(actual).hasSize(1);

	}

	@DisplayName("It checks delete by id")
	@Test
	void itCanDeleteAStudentById() {
		Long id = 1L;
		underTest.deleteStudentById(id);
		verify(studentRepository).deleteById(id);
	}

	@DisplayName("It should check whether a student is created")
	@Test
	void itCanCreateAStudent() {
		StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);

		when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

		StudentDTO actual = underTest.createNewStudent(studentDTO);

		ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

		verify(studentRepository).save(studentCaptor.capture());

		Student capturedStudent = studentCaptor.getValue();

		assertThat(capturedStudent).usingRecursiveComparison().isEqualTo(student);
		assertThat(actual).usingRecursiveComparison().isEqualTo(student);
	}

	@DisplayName("It should get a student by Id")
	@Test
	void itCanGetAStudentById() {
		Long id = 1L;

		when(studentRepository.findById(id)).thenReturn(Optional.of(student));

		StudentDTO studentDTO = underTest.getStudentById(id);

		verify(studentRepository).findById(id);

		assertThat(studentDTO).usingRecursiveComparison().isEqualTo(student);
	}

	@DisplayName("It should check a student can be updated")
	@Test
	void itShouldUpdateAStudent() {
		
		Long id = 1L;

		when(studentRepository.findById(id)).thenReturn(Optional.of(student));
		
		student.setEducation("Btech in Cse");
		StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
		
		when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

		
		StudentDTO actual = underTest.updateStudent(id, studentDTO);

		ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

		verify(studentRepository).findById(id);
		verify(studentRepository).save(studentCaptor.capture());

		Student capturedStudent = studentCaptor.getValue();

		assertThat(capturedStudent).usingRecursiveComparison().isEqualTo(student);
		assertThat(actual).usingRecursiveComparison().isEqualTo(student);
	}

	@DisplayName("It should a check a student can be patched")
	@Test
	void itShouldPatchAStudent() {
		Long id = 1L;

		when(studentRepository.findById(id)).thenReturn(Optional.of(student));
		
		student.setEducation("Btech in Cse");
		student.setName("Mr Aditya Shukla");
		
		StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
		
		when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);

		
		StudentDTO actual = underTest.patchStudent(id, studentDTO);

		ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);

		verify(studentRepository).findById(id);
		verify(studentRepository).save(studentCaptor.capture());

		Student capturedStudent = studentCaptor.getValue();

		assertThat(capturedStudent).usingRecursiveComparison().isEqualTo(student);
		assertThat(actual).usingRecursiveComparison().isEqualTo(student);
	}
}
