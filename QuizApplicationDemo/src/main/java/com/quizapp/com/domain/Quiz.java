package com.quizapp.com.domain;

import java.time.LocalDateTime;
import java.util.HashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Quiz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "quiz_title", nullable = false)
	private String quizTitle;

	@Column(name = "quiz_description", nullable = true)
	private String quizDescription;

	@OneToMany(mappedBy = "quiz", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	private Set<Question> questions = new HashSet<>();

	@OneToMany(mappedBy = "quiz",cascade = CascadeType.REMOVE)
	private Set<StudentQuiz> studentquiz = new HashSet<>();

	@Column(name = "create_time")
	private LocalDateTime quizCreateDateTime;

	@Column(columnDefinition = "TINYINT", length = 1)
	private Boolean isRunning;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "quiz_topics", joinColumns = @JoinColumn(name = "quiz_id"), inverseJoinColumns = @JoinColumn(name = "topic_id"))
	private Set<Topic> topics = new HashSet<>();

	public Quiz() {

	}

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

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Set<StudentQuiz> getStudentquiz() {
		return studentquiz;
	}

	public void setStudentquiz(Set<StudentQuiz> studentquiz) {
		this.studentquiz = studentquiz;
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

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	public Quiz addQuestion(Question ques) {
		ques.setQuiz(this);
		this.questions.add(ques);
		return this;
	}

	public void addTopic(Topic topic) {
		if (topics.contains(topic))
			return;

		topics.add(topic);

		topic.addQuiz(this);
	}

	public void addStudent(StudentQuiz st) {

		if (studentquiz.contains(st))
			return;

		this.studentquiz.add(st);

		st.getStudent().addQuiz(st);

	}

	public void removeTopic(Topic topic) {

		if (!topics.contains(topic))
			return;

		topics.remove(topic);

		topic.removeQuiz(this);

	}
	
	public void removeQuestion(Question ques)
	{
		if(!questions.contains(ques))
			return;
		
		questions.remove(ques);
		
		ques.setQuiz(null);
	}
}
