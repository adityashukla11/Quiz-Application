package com.quizapp.com.payload;


public class StudentQuizDTO {

	private StudentDTO student;
	
	private Integer score;

 
	public StudentQuizDTO()
	{
		
	}
	public StudentQuizDTO(StudentDTO student, Integer score) {
		this.student = student;
		this.score = score;
	}

	public StudentDTO getStudent() {
		return student;
	}

	public void setStudent(StudentDTO student) {
		this.student = student;
	}

	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	
}
