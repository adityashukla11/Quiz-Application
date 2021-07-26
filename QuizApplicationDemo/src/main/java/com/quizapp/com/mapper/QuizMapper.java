package com.quizapp.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.quizapp.com.domain.Quiz;
import com.quizapp.com.payload.QuizDTO;
import com.quizapp.com.payload.QuizPatchDTO;

@Mapper
public interface QuizMapper
{
	QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);
	
	QuizDTO quizToQuizDTO(Quiz quiz);
	
	Quiz quizDTOtoQuiz(QuizDTO quizDTO);
	
	QuizPatchDTO quizToQuizPatchDTO(Quiz quiz);
	
	Quiz quizPatchDTOToQuiz(QuizPatchDTO quizPatchDTO);
}