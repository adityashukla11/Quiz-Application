package com.quizapp.com.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TopicPatchDTO {


	private Long id;
	
	private String topicCode;
	

	private String topicName;


	private String topicDescription;


	public TopicPatchDTO(Long id, String topicCode, String topicName, String topicDescription) {
		this.id = id;
		this.topicCode = topicCode;
		this.topicName = topicName;
		this.topicDescription = topicDescription;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTopicCode() {
		return topicCode;
	}


	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}


	public String getTopicName() {
		return topicName;
	}


	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}


	public String getTopicDescription() {
		return topicDescription;
	}


	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}
	

	
	
}
