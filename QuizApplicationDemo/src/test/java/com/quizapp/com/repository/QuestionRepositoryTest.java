package com.quizapp.com.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.quizapp.com.domain.Question;
import com.quizapp.com.repository.QuestionRepository;


@DataJpaTest
public class QuestionRepositoryTest {

	private QuestionRepository underTest;
	private Question ques1;
	private Question ques2;

	@BeforeEach
	void setUp() {
		
		ques1 = new Question();
		ques2 = new Question();
		ques1.setDescription("What is the capital of India");
		ques2.setDescription("Who is the president of India");
		
		underTest.save(ques1);
		underTest.save(ques2);
	}
	
	@AfterEach
	void tearDown()
	{
		underTest.deleteAll();
	}
	
	@DisplayName("Search by Quiz Description should exist")
	void doesSearchByDescriptionExist()
	{
		
		Set<Question> expected = underTest.findAllByDescriptionContains("Ind");
		
		assertThat(expected).hasSize(2).allSatisfy(question -> question.getDescription().toLowerCase().contains("ind"));
	}
	
}
