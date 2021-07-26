package com.quizapp.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.quizapp.com.domain.Topic;
import com.quizapp.com.payload.TopicDTO;
import com.quizapp.com.payload.TopicPatchDTO;

@Mapper
public interface TopicMapper {

	TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);
	
	TopicDTO topicToTopicDTO(Topic topic);
	
	Topic topicDTOtoTopic(TopicDTO topicDTO);
	
	TopicPatchDTO topicToTopicPatchDTO(Topic topic);
	
	Topic topicPatchDTOtoTopic(TopicPatchDTO topicPatchDTO);
}
