package com.quizapp.com.payload;

import java.time.LocalDateTime;

public class QuizPatchDTO {

	private Long id;

	private String quizTitle;

	private String quizDescription;

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

	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}

	public String getQuizDescription() {
		return quizDescription;
	}

	public void setQuizDescription(String quizDescription) {
		this.quizDescription = quizDescription;
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
	
	

}
