package com.quizapp.com.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.TopicMapper;
import com.quizapp.com.mapper.TopicMapperImpl;
import com.quizapp.com.payload.TopicDTO;
import com.quizapp.com.payload.TopicPatchDTO;
import com.quizapp.com.payload.lists.TopicIdListDTO;
import com.quizapp.com.repository.QuizRepository;
import com.quizapp.com.repository.TopicRepository;
import com.quizapp.com.services.implementation.TopicServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

	@Mock
	private TopicRepository topicRepository;
	@Mock
	private QuizRepository quizRepository;

	private TopicService underTest;

	@InjectMocks
	private TopicMapperImpl topicMapper;

	private Topic topic1;
	private Topic topic2;

	@BeforeEach
	void setUp() {

		underTest = new TopicServiceImpl(topicRepository, topicMapper, quizRepository);
		topic1 = new Topic();
		topic2 = new Topic();

		topic1.setId(1L);
		topic1.setTopicCode("CH2021");
		topic1.setTopicDescription("A quiz on Chemistry");
		topic1.setTopicName("Chemistry");

		topic2.setId(2L);
		topic2.setTopicCode("PH2021");
		topic2.setTopicDescription("A quiz on Physics");
		topic2.setTopicName("Physics");

	}

	@DisplayName("It should get all topics that exists")
	@Test
	void itShouldGetAllTopics() {

		List<Topic> expected = new ArrayList<>();

		expected.add(topic1);
		expected.add(topic2);

		doReturn(expected).when(topicRepository).findAll();

		Set<TopicDTO> actual = underTest.getAllTopics();

		verify(topicRepository).findAll();

		assertThat(actual).hasSize(2);

		assertThat(actual).extracting("topicCode").containsExactlyInAnyOrder(topic1.getTopicCode(),
				topic2.getTopicCode());

	}

	@DisplayName("It should get a topic by Id")
	@Test
	void itShouldGetTopicById() {

		Long id = 1L;

		doReturn(Optional.of(topic1)).when(topicRepository).findById(id);

		TopicDTO actual = underTest.getTopicById(id);

		verify(topicRepository).findById(id);

		assertThat(actual).isNotNull();

		assertThat(actual).extracting("topicCode", "topicName", "topicDescription").contains(topic1.getTopicCode(),
				topic1.getTopicName(), topic1.getTopicDescription());

	}

	@DisplayName("It should check that a new topic is created")
	@Test
	void itShouldCreateNewTopic() {

		TopicPatchDTO topicPatchDTO = topicMapper.topicToTopicPatchDTO(topic1);

		when(topicRepository.save(Mockito.any(Topic.class))).thenReturn(topic1);

		TopicPatchDTO topicDTO = underTest.createNewTopic(topicPatchDTO);

		ArgumentCaptor<Topic> argumentCaptor = ArgumentCaptor.forClass(Topic.class);

		verify(topicRepository).save(argumentCaptor.capture());

		Topic capturedTopic = argumentCaptor.getValue();

		assertThat(capturedTopic).usingRecursiveComparison().isEqualTo(topic1);
		assertThat(topicDTO).extracting("id", "topicCode", "topicName", "topicDescription").contains(topic1.getId(),
				topic1.getTopicCode(), topic1.getTopicName(), topic1.getTopicDescription());

	}

	@DisplayName("It should check that a id is deleted")
	@Test
	void itShouldDeleteTopic() {
		Long id = 1L;

		when(topicRepository.findById(id)).thenReturn(Optional.of(topic1));
		
		underTest.deleteTopicById(id);

		verify(topicRepository).deleteById(id);
	}

	@DisplayName("It should check that a topic search by name exists")
	@Test
	void itShouldSearchTopicByName() {
		Set<Topic> expected = new HashSet<Topic>();
		expected.add(topic1);

		doReturn(expected).when(topicRepository).findAllByTopicNameContains("Chem");

		Set<TopicDTO> actual = underTest.searchTopicByName("Chem");

		assertThat(actual).hasSize(1);

		assertThat(actual).allSatisfy(topic -> topic.getTopicName().contains("Chem"));
	}

	@DisplayName("It should check that the topic is updated")
	@Test
	void itShouldUpdateTopic() {

		Long id = 1L;

		doReturn(Optional.of(topic1)).when(topicRepository).findById(id);

		when(topicRepository.save(Mockito.any(Topic.class))).thenReturn(topic1);
		

		topic1.setTopicCode("CHE2021");

		TopicPatchDTO topicPatchDTO = topicMapper.topicToTopicPatchDTO(topic1);

		ArgumentCaptor<Topic> topicCaptor = ArgumentCaptor.forClass(Topic.class);

		TopicPatchDTO actual = underTest.updateTopic(id, topicPatchDTO);

		verify(topicRepository).findById(id);

		verify(topicRepository).save(topicCaptor.capture());

		Topic capturedTopic = topicCaptor.getValue();

		assertThat(capturedTopic).usingRecursiveComparison().isEqualTo(topic1);

		assertEquals(actual.getTopicCode(), topic1.getTopicCode());

	}
	
	@DisplayName("It should check that the topic is updated")
	@Test
	void itShouldPatchTopic() {

		Long id = 1L;

		doReturn(Optional.of(topic1)).when(topicRepository).findById(id);

		when(topicRepository.save(Mockito.any(Topic.class))).thenReturn(topic1);
		
		topic1.setTopicCode("CHE2021");

		TopicPatchDTO topicPatchDTO = topicMapper.topicToTopicPatchDTO(topic1);

		ArgumentCaptor<Topic> topicCaptor = ArgumentCaptor.forClass(Topic.class);

		TopicPatchDTO actual = underTest.patchTopic(id, topicPatchDTO);

		verify(topicRepository).findById(id);

		verify(topicRepository).save(topicCaptor.capture());

		Topic capturedTopic = topicCaptor.getValue();

		assertThat(capturedTopic).usingRecursiveComparison().isEqualTo(topic1);

		assertThat(actual).usingRecursiveComparison().isEqualTo(topic1);

	}
	
	@DisplayName("It should add all topics to a quiz")
	@Test
	void itShouldAddTopicsToQuiz()
	{
		Quiz quiz1 = new Quiz();
		quiz1.setId(1L);
		quiz1.setQuizTitle("Maths Quiz");
		quiz1.setQuizDescription("This is a maths quiz");
		quiz1.setIsRunning(true);
		quiz1.setQuizCreateDateTime(LocalDateTime.of(2021, 6, 19, 0, 0, 0));
	
		when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz1));
		when(topicRepository.findById(1L)).thenReturn(Optional.of(topic1));
		when(topicRepository.findById(2L)).thenReturn(Optional.of(topic2));
		
		List<Long> topicIds = new ArrayList<>();
		topicIds.add(topic1.getId());
		topicIds.add(topic2.getId());
		
		TopicIdListDTO topicIdListDTO = new TopicIdListDTO(topicIds);
		
		underTest.addQuizToTopics(1L, topicIdListDTO);
		
		verify(quizRepository,times(1)).findById(1L);
		assertThat(quiz1.getTopics()).containsExactlyInAnyOrder(topic1,topic2);
		assertThat(topic1.getQuizzes()).contains(quiz1);

	}
}
