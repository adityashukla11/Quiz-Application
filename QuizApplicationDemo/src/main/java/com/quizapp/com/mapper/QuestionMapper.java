package com.quizapp.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.quizapp.com.domain.Question;
import com.quizapp.com.payload.QuestionDTO;

@Mapper
public interface QuestionMapper
{

	QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
	
	QuestionDTO questionToQuestionDTO(Question question);
	
	@Mapping(target = "correctOption", ignore = true)
	@Mapping(target = "quiz", ignore = true)
	Question questionDTOtoQuestion(QuestionDTO questionDTO);
}