package com.quizapp.com.domain;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class Question {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ques_description", nullable =false)
	private String description;
	
	
	@ManyToOne
	@JoinTable(name = "ques_quiz", joinColumns = @JoinColumn(name = "ques_id"), inverseJoinColumns = @JoinColumn(name = "quiz_id"))
	private Quiz quiz;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Option> options = new HashSet<>();
	
	
	
	@OneToOne(cascade = CascadeType.ALL)
	private Option correctOption;
	
	
	public Question()
	{
		
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Quiz getQuiz() {
		return quiz;
	}


	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}


	public Set<Option> getOptions() {
		return options;
	}


	public void setOptions(Set<Option> options) {
		this.options = options;
	}


	public Option getCorrectOption() {
		return correctOption;
	}


	public void setCorrectOption(Option correctOption) {
		this.correctOption = correctOption;
	}
}
