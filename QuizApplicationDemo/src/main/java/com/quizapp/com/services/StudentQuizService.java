package com.quizapp.com.services;

import java.util.List;

import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.payload.StudentQuizDTO;

public interface StudentQuizService {
	
 StudentQuizDTO findByQuizIdAndStudentId(Long quizId, Long studentId);
 List<StudentQuizDTO> getLeaderboardForQuiz(Long id);
 List<StudentQuizDTO> getfailedStudents(Long id, Integer passingMarks);
 StudentQuizDTO updateScoreOfaStudent(Long studId,Long quizId, Integer score);
 
}
