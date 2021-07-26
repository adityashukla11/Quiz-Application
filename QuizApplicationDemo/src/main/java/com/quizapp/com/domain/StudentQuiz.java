package com.quizapp.com.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class StudentQuiz {

	@EmbeddedId
	private StudentQuizScore id;

	public StudentQuiz() {

	}

	public StudentQuiz(StudentQuizScore id, Student student, Quiz quiz, Integer score) {
		this.id = id;
		this.student = student;
		this.quiz = quiz;
		this.score = score;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("studentId")
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("quizId")
	private Quiz quiz;

	private Integer score;

	public StudentQuizScore getId() {
		return id;
	}

	public void setId(StudentQuizScore id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
