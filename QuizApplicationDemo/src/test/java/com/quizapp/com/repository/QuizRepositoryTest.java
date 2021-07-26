package com.quizapp.com.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.repository.QuizRepository;

@DataJpaTest
public class QuizRepositoryTest {

	@Autowired
	private QuizRepository underTest;

	private Quiz quiz1;
	private Quiz quiz2;
	private Topic topic1;
	private Topic topic2;

	@BeforeEach
	void setUp() {
		topic1 = new Topic();
		topic1.setTopicCode("CH2021");
		topic1.setTopicName("Chemistry");

		topic2 = new Topic();
		topic2.setTopicCode("PH2021");
		topic2.setTopicName("PHYSICS");

		quiz1 = new Quiz();
		quiz2 = new Quiz();

		quiz1.setQuizCreateDateTime(LocalDateTime.of(2021,6,19,0,0,0));
		quiz1.setQuizDescription("A quiz on Physics");
		quiz1.setIsRunning(true);
		quiz1.setQuizTitle("Physics - Magnetics");
		quiz1.getTopics().add(topic1);

		quiz2.setQuizCreateDateTime(LocalDateTime.of(2021,6,25,0,0,0));
		quiz2.setQuizDescription("An exiciting quiz on Physics");
		quiz2.setIsRunning(false);
		quiz2.setQuizTitle("Physics - Mania");
		quiz2.getTopics().add(topic2);

		underTest.save(quiz1);
		underTest.save(quiz2);

	}

	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}

	@DisplayName("It checks whether search by Topic name exists")
	@Test
	void itShouldCheckQuizSearchByTopicNameExists() {
		List<String> topicNames = new ArrayList<>();

		topicNames.add("Chemistry");
		topicNames.add("Physics");

		Set<Quiz> expectedQuiz = underTest.findAllByTopicsTopicNameIn(topicNames);

		assertThat(expectedQuiz).hasSize(1).allSatisfy(quiz -> {
			assertThat(quiz.getQuizTitle().toLowerCase()).contains("phy");
		});
	}

	@DisplayName("It checks all running quiz exists.")
	@Test
	void itShouldCheckisRunningQuizTrue() {
		Boolean running = true;

		Set<Quiz> expectedQuiz = underTest.findByIsRunningEquals(running);

		assertThat(expectedQuiz).hasSize(1);
	}

	@DisplayName("It checks all previous exists.")
	@Test
	void itShouldCheckisRunningQuizFalse() {
		Boolean running = false;

		Set<Quiz> expectedQuiz = underTest.findByIsRunningEquals(running);

		assertThat(expectedQuiz).hasSize(1);
	}
	
	@DisplayName("This checks that all quiz before a certain date exists")
	@Test
	void itShouldCheckAllQuizBeforeDateExist()
	{
		LocalDateTime dateTime = LocalDateTime.of(2021,6,23,0,0,0);
		
		Set<Quiz> expectedQuiz = underTest.findByQuizCreateDateTimeBefore(dateTime);
		
		assertThat(expectedQuiz).hasSize(1);
	}
	
	
	@DisplayName("This checks that all quiz before a certain date does not exists")
	@Test
	void itShouldCheckAllQuizBeforeDateDoesNotExist()
	{
		LocalDateTime dateTime = LocalDateTime.of(2020,6,15,0,0,0);
		
		Set<Quiz> expectedQuiz = underTest.findByQuizCreateDateTimeBefore(dateTime);
		
		assertThat(expectedQuiz).hasSize(0);
	}


}
