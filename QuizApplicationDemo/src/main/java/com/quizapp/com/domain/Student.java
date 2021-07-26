package com.quizapp.com.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "student_name", nullable = false)
	private String name;
	
	@Column(name = "student_education", nullable = false)
	private String education;
	
	@OneToMany(mappedBy = "student",cascade = CascadeType.REMOVE)
	private Set<StudentQuiz> studentquiz= new HashSet<>();

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public Set<StudentQuiz> getStudentquiz() {
		return studentquiz;
	}

	public void setStudentquiz(Set<StudentQuiz> studentquiz) {
		this.studentquiz = studentquiz;
	}

	public void addQuiz(StudentQuiz stQuiz) {
		if (studentquiz.contains(stQuiz)) {
			return;
		}

		this.studentquiz.add(stQuiz);
		
		stQuiz.getQuiz().addStudent(stQuiz);
		
	}
	
	
}
