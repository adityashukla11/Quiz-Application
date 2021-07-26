package com.quizapp.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.quizapp.com.domain.Student;
import com.quizapp.com.payload.StudentDTO;

@Mapper
public interface StudentMapper {

StudentMapper MAPPER = Mappers.getMapper(StudentMapper.class);
	
	StudentDTO studentToStudentDTO(Student student);
	
	Student studentDTOtoStudent(StudentDTO studentDTO);
	
}
