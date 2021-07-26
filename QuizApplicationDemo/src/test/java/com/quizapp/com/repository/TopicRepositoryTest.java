package com.quizapp.com.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.quizapp.com.domain.Topic;
import com.quizapp.com.repository.TopicRepository;

@DataJpaTest
public class TopicRepositoryTest {

	@Autowired
	private TopicRepository underTest;
	private Topic testTopic;

	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}

	@BeforeEach
	void setUp() {

		testTopic = new Topic();
		testTopic.setTopicName("Chemistry");
		testTopic.setTopicDescription("Test of Chemistry Test");
		testTopic.setTopicCode("CH19");
		underTest.save(testTopic);

	}

	@DisplayName("Search by topic name exists")
	@Test
	void isShouldCheckFindByTopicNameSearchExists()
	{	
		
		Set<Topic> topics = underTest.findAllByTopicNameContains("Chem");
		
		assertThat(topics).hasSize(1).allMatch(topic -> topic.getTopicName().startsWith("Chem"));
	}
}
