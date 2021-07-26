package com.quizapp.com.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizapp.com.domain.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>{
	
    Set<Topic> findAllByTopicNameContains(String keyword);

}
