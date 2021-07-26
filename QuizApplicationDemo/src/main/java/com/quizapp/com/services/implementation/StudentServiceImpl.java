package com.quizapp.com.services.implementation;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Student;
import com.quizapp.com.mapper.StudentMapper;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.repository.StudentRepository;
import com.quizapp.com.services.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;

	public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
		this.studentRepository = studentRepository;
		this.studentMapper = studentMapper;
	}

	@Override
	public Set<StudentDTO> getAllStudents() {
		return studentRepository.findAll().stream().map(studentMapper::studentToStudentDTO).collect(Collectors.toSet());
	}

	@Override
	public void deleteStudentById(Long id) {
		studentRepository.deleteById(id);
	}

	@Override
	public StudentDTO createNewStudent(StudentDTO student) {
		return saveAndReturnDTO(studentMapper.studentDTOtoStudent(student));
	}

	@Override
	public StudentDTO getStudentById(Long id) {
		return studentRepository.findById(id).map(studentMapper::studentToStudentDTO).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
		return studentRepository.findById(id).map(student -> {
			student.setEducation(studentDTO.getEducation());
			student.setName(studentDTO.getName());

			StudentDTO returnDTO = studentMapper.studentToStudentDTO(studentRepository.save(student));

			return returnDTO;

		}).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public StudentDTO patchStudent(Long id, StudentDTO studentDTO) {
		return studentRepository.findById(id).map(student -> {

			if (studentDTO.getEducation() != null)
				student.setEducation(studentDTO.getEducation());

			if (studentDTO.getName() != null)
				student.setName(studentDTO.getName());

			StudentDTO returnDTO = studentMapper.studentToStudentDTO(studentRepository.save(student));

			return returnDTO;
		}).orElseThrow(ResourceNotFoundException::new);
	}

	private StudentDTO saveAndReturnDTO(Student student) {
		Student savedStudent = studentRepository.save(student);

		return studentMapper.studentToStudentDTO(savedStudent);
	}

}
