package com.quizapp.com.payload.lists;

import java.util.Set;

import com.quizapp.com.payload.StudentDTO;


public class StudentListDTO {

	private Set<StudentDTO> students;


	public StudentListDTO(Set<StudentDTO> students) {
		this.students = students;
	}

	public Set<StudentDTO> getStudents() {
		return students;
	}

	public void setStudents(Set<StudentDTO> students) {
		this.students = students;
	}

	
	
	
}
