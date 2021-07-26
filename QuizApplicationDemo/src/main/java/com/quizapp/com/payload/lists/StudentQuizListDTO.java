package com.quizapp.com.payload.lists;

import java.util.List;

import com.quizapp.com.payload.StudentQuizDTO;

import lombok.Data;

public class StudentQuizListDTO {

	private List<StudentQuizDTO> studentquiz;
	
	public StudentQuizListDTO(List<StudentQuizDTO> studentquiz) {
		this.studentquiz = studentquiz;
	}

	public List<StudentQuizDTO> getStudentquiz() {
		return studentquiz;
	}

	public void setStudentquiz(List<StudentQuizDTO> studentquiz) {
		this.studentquiz = studentquiz;
	}


	
	
}
