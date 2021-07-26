package com.quizapp.com.services;

import java.time.LocalDateTime;
import java.util.Set;

import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.payload.QuizDTO;
import com.quizapp.com.payload.QuizPatchDTO;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.StudentIdDTO;
import com.quizapp.com.payload.lists.QuestionIdListDTO;

public interface QuizService {

	Set<QuizDTO> getAllQuiz();
	Set<QuizDTO> getAllRunningQuiz();
	Set<QuizDTO> getAllpreviousQuiz();
	Set<StudentDTO> getAllRegisteredStudents(Long id);
	Set<QuestionDTO> getAllQuestion(Long id);
	Set<QuizDTO> getAllQuizBeforeDate(LocalDateTime dateTime);
	QuizDTO getQuiz(Long id);
	QuizPatchDTO createQuiz(QuizPatchDTO quiz);
	QuizPatchDTO patchQuiz(Long id , QuizPatchDTO quizDTO);
	QuizPatchDTO updateQuiz(Long id,QuizPatchDTO quizPatchDTO);
	void deleteQuiz(Long id);
	
	void addQuestionToQuiz(Long quizId,QuestionIdListDTO quizQuestionListDTO);
	void addStudentToQuiz(Long quizId,StudentIdDTO studentIdDTO);
	
}
