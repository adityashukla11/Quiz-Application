package com.quizapp.com.services;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.quizapp.com.payload.QuestionDTO;

public interface QuestionService {

	Set<QuestionDTO> getAllQuestion();
	
	Set<QuestionDTO> getQuestionByKeywords(String keywords);
	
	QuestionDTO getQuestion(Long id);
	
	QuestionDTO createQuestion(QuestionDTO questionDTO);
	
	QuestionDTO updateQuestion(Long id,QuestionDTO questionDTO);
	
	QuestionDTO patchQuestion(Long id,QuestionDTO questionDTO);
	
	void deleteQuestion(Long id);
}
