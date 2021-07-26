package com.quizapp.com.payload.lists;

import java.util.Set;

import com.quizapp.com.payload.QuestionDTO;


public class QuestionListDTO {
	
	private Set<QuestionDTO> questions;

	public QuestionListDTO(Set<QuestionDTO> questions) {
		this.questions = questions;
	}

	public Set<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<QuestionDTO> questions) {
		this.questions = questions;
	}
	
	
}
