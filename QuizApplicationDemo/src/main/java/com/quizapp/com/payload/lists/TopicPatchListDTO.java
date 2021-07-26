package com.quizapp.com.payload.lists;

import java.util.Set;

import com.quizapp.com.payload.TopicPatchDTO;

import lombok.Data;

public class TopicPatchListDTO {

	private Set<TopicPatchDTO> topics;

	public TopicPatchListDTO(Set<TopicPatchDTO> topics) {
		this.topics = topics;
	}

	public Set<TopicPatchDTO> getTopics() {
		return topics;
	}

	public void setTopics(Set<TopicPatchDTO> topics) {
		this.topics = topics;
	}
	
	
}
