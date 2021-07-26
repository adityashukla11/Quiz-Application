package com.quizapp.com.payload;

import java.util.HashSet;
import java.util.Set;


public class TopicDTO {

	private Long id;

	
	private String topicCode;
	

	private String topicName;


	private String topicDescription;

	private Set<QuizDTO> quizzes = new HashSet<>();

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

	public Set<QuizDTO> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(Set<QuizDTO> quizzes) {
		this.quizzes = quizzes;
	}

	
}
