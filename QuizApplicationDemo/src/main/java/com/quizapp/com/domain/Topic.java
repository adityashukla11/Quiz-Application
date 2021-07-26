package com.quizapp.com.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.quizapp.com.mapper.QuizMapperImpl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "topic_code", nullable = false, unique = true)
	private String topicCode;

	@Column(name = "topic_name", nullable = false)
	private String topicName;

	@Column(name = "topic_description")
	private String topicDescription;

	@ManyToMany(mappedBy = "topics")
	private Set<Quiz> quizzes = new HashSet<>();

	public Topic() {

	}

	public Topic(Long id, String topicCode, String topicName, String topicDescription, Set<Quiz> quizzes) {
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

	public Set<Quiz> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(Set<Quiz> quizzes) {
		this.quizzes = quizzes;
	}

	public void addQuiz(Quiz quiz) {
		if (quizzes.contains(quiz))
			return;

		quizzes.add(quiz);

		quiz.addTopic(this);
	}

	public void removeQuiz(Quiz quiz) {
		if (!quizzes.contains(quiz))
			return;
		quizzes.remove(quiz);
		
		quiz.removeTopic(this);
	}

}
