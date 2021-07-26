package com.quizapp.com.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
public class StudentQuizScore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentQuizScore() {

	}

	public StudentQuizScore(Long studentId, Long quizId) {
		this.studentId = studentId;
		this.quizId = quizId;
	}

	@Column(name = "student_id")
	private Long studentId;

	@Column(name = "quiz_id")
	private Long quizId;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

}
