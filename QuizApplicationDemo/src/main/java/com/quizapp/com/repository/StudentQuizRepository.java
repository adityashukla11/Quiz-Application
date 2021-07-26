package com.quizapp.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;

@Repository
public interface StudentQuizRepository extends JpaRepository<StudentQuiz, StudentQuizScore> {
	

List<StudentQuiz> findAllById_QuizId(long id);

List<StudentQuiz> findAllByIdQuizId(Long l);

List<StudentQuiz> findByIdQuizId(Long l);

List<StudentQuiz> findAllByIdQuizIdOrderByScoreDesc(Long l);


  
}