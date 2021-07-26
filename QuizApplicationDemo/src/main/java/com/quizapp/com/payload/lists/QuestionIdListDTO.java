package com.quizapp.com.payload.lists;

import java.util.List;


public class QuestionIdListDTO {

	List<Long> quizIds;
	
	public QuestionIdListDTO(List<Long> quizIds) {
		this.quizIds = quizIds;
	}

	
	public QuestionIdListDTO() {
	}

	public List<Long> getQuizIds() {
		return quizIds;
	}

	public void setQuizIds(List<Long> quizIds) {
		this.quizIds = quizIds;
	}

	
	
	
	
	
}
