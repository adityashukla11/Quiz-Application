package com.quizapp.com.services;

import java.util.List;
import java.util.Set;

import com.quizapp.com.payload.TopicDTO;
import com.quizapp.com.payload.TopicPatchDTO;
import com.quizapp.com.payload.lists.TopicIdListDTO;

public interface TopicService {
	
	
 Set<TopicDTO> getAllTopics();
 
 TopicDTO getTopicById(Long id);
 
 TopicPatchDTO createNewTopic(TopicPatchDTO topic);
 
 TopicPatchDTO patchTopic(Long id, TopicPatchDTO topicDTO );
 
 TopicPatchDTO updateTopic(Long id, TopicPatchDTO topicPatchDTO);
 
 void deleteTopicById(Long id);
 
 Set<TopicDTO> searchTopicByName(String keyword);
 
 void addQuizToTopics(Long quizId,TopicIdListDTO topicIdListDTO);
 
}
