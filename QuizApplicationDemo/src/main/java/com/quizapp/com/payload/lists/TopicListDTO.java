package com.quizapp.com.payload.lists;

import java.util.Set;

import com.quizapp.com.payload.TopicDTO;

import lombok.Getter;
import lombok.Setter;



public class TopicListDTO {

	private Set<TopicDTO> topics;

	public TopicListDTO(Set<TopicDTO> topics) {
		this.topics = topics;
	}

	public Set<TopicDTO> getTopics() {
		return topics;
	}

	public void setTopics(Set<TopicDTO> topics) {
		this.topics = topics;
	}

	
	
	
}
