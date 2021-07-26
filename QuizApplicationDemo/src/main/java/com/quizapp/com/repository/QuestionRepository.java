package com.quizapp.com.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizapp.com.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question ,Long>{
	
  Set<Question> findAllByDescriptionContains(String keyword);

}
