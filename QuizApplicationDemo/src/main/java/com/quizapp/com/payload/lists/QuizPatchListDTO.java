package com.quizapp.com.payload.lists;

import java.util.Set;

import com.quizapp.com.payload.QuizPatchDTO;

public class QuizPatchListDTO {

	private Set<QuizPatchDTO> quizzes;

	
	public QuizPatchListDTO(Set<QuizPatchDTO> quizzes) {
		this.quizzes = quizzes;
	}

	public Set<QuizPatchDTO> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(Set<QuizPatchDTO> quizzes) {
		this.quizzes = quizzes;
	}
	
	
}
