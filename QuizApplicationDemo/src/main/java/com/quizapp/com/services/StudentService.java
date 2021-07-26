package com.quizapp.com.services;

import java.util.Set;

import com.quizapp.com.domain.Student;
import com.quizapp.com.payload.StudentDTO;

public interface StudentService {
	
 Set<StudentDTO> getAllStudents();
 
 void deleteStudentById(Long id);
 
 StudentDTO createNewStudent(StudentDTO student);
 
 StudentDTO getStudentById(Long id);
 
 StudentDTO updateStudent(Long id, StudentDTO studentDTO);
 
 StudentDTO patchStudent(Long id, StudentDTO studentDTO);
}
