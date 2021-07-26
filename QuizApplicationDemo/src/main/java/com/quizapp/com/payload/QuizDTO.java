package com.quizapp.com.payload;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




public class QuizDTO {
	
	private Long id;
	
	private String quizTitle;
	
	private String quizDescription;
	
	private Set<QuestionDTO> questions;
	
	private Set<TopicPatchDTO> topics;

	private LocalDateTime quizCreateDateTime;
	
	private Boolean isRunning;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public void setQuizTitle(String title) {
		this.quizTitle = title;
	}

	public String getQuizDescription() {
		return quizDescription;
	}

	public void setQuizDescription(String quizDescription) {
		this.quizDescription = quizDescription;
	}

	public Set<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<QuestionDTO> questions) {
		this.questions = questions;
	}

	public Set<TopicPatchDTO> getTopics() {
		return topics;
	}

	public void setTopics(Set<TopicPatchDTO> topics) {
		this.topics = topics;
	}

	public LocalDateTime getQuizCreateDateTime() {
		return quizCreateDateTime;
	}

	public void setQuizCreateDateTime(LocalDateTime quizCreateDateTime) {
		this.quizCreateDateTime = quizCreateDateTime;
	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public String toString() {
		return "QuizDTO [id=" + id + ", quizTitle=" + quizTitle + ", quizDescription=" + quizDescription
				+ ", questions=" + questions + ", topics=" + topics + ", quizCreateDateTime=" + quizCreateDateTime
				+ ", isRunning=" + isRunning + "]";
	}

	

	
	
	
}