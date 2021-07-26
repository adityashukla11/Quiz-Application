package com.quizapp.com.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Topic;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {

	Set<Quiz> findAllByTopicsTopicNameIn(List<String> topicNames);
	
	Set<Quiz> findByIsRunningEquals(Boolean isRunning);
	
	Set<Quiz> findByQuizCreateDateTimeBefore(LocalDateTime dateTime);
	
}
 