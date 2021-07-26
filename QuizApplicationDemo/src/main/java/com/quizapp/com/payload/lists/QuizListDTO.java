package com.quizapp.com.payload.lists;

import java.util.Set;

import com.quizapp.com.payload.QuizDTO;

import lombok.Data;

@Data
public class QuizListDTO {

	private Set<QuizDTO> quizzes;

	public QuizListDTO(Set<QuizDTO> quizzes) {
		this.quizzes = quizzes;
	}

	public Set<QuizDTO> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(Set<QuizDTO> quizzes) {
		this.quizzes = quizzes;
	}

}
