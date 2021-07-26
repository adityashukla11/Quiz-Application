package com.quizapp.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.payload.StudentQuizDTO;

@Mapper
public interface StudentScoreMapper {

	StudentScoreMapper MAPPER = Mappers.getMapper(StudentScoreMapper.class);
	

	StudentQuizDTO studentQuiztoStudentQuizDTO(StudentQuiz studentQuiz);
}
