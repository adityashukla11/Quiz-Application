package com.quizapp.com.services.implementation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.TopicMapper;
import com.quizapp.com.payload.TopicDTO;
import com.quizapp.com.payload.TopicPatchDTO;
import com.quizapp.com.payload.lists.TopicIdListDTO;
import com.quizapp.com.repository.QuizRepository;
import com.quizapp.com.repository.TopicRepository;
import com.quizapp.com.services.TopicService;

@Service
public class TopicServiceImpl implements TopicService {

	private final TopicRepository topicRepository;
	private final TopicMapper topicMapper;
	private final QuizRepository quizRepository;

	public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper, QuizRepository quizRepository) {
		this.topicRepository = topicRepository;
		this.topicMapper = topicMapper;
		this.quizRepository = quizRepository;
	}

	@Override
	public Set<TopicDTO> getAllTopics() {

		return topicRepository.findAll().stream().map(topicMapper::topicToTopicDTO).collect(Collectors.toSet());

	}

	@Override
	public TopicDTO getTopicById(Long id) {
		return topicRepository.findById(id).map(topicMapper::topicToTopicDTO)
				.orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public TopicPatchDTO createNewTopic(TopicPatchDTO topicDTO) {

		return saveAndReturnDTO(topicMapper.topicPatchDTOtoTopic(topicDTO));
	}

	@Override
	public TopicPatchDTO patchTopic(Long id, TopicPatchDTO topicPatchDTO) {
		return topicRepository.findById(id).map(topic -> {

			if (topicPatchDTO.getTopicCode() != null)
				topic.setTopicCode(topicPatchDTO.getTopicCode());

			if (topicPatchDTO.getTopicName() != null)
				topic.setTopicName(topicPatchDTO.getTopicName());

			if (topicPatchDTO.getTopicDescription() != null)
				topic.setTopicDescription(topicPatchDTO.getTopicDescription());

			TopicPatchDTO returnDTO = topicMapper.topicToTopicPatchDTO(topicRepository.save(topic));

			return returnDTO;

		}).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public void deleteTopicById(Long id) {

		Topic topic = topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

		Set<Quiz> quizs = topic.getQuizzes();

		for (Quiz quiz : quizs) {
			topic.removeQuiz(quiz);
		}

		topicRepository.deleteById(id);

	}

	@Override
	public Set<TopicDTO> searchTopicByName(String keyword) {
		return topicRepository.findAllByTopicNameContains(keyword).stream().map(topicMapper::topicToTopicDTO)
				.collect(Collectors.toSet());
	}

	private TopicPatchDTO saveAndReturnDTO(Topic topic) {
		// Persist the Topic in the database
		Topic savedTopic = new Topic();
		System.out.println(savedTopic.getId());
		savedTopic = topicRepository.save(topic);
		return topicMapper.topicToTopicPatchDTO(savedTopic);
	}

	@Override
	public TopicPatchDTO updateTopic(Long id, TopicPatchDTO topicPatchDTO) {

		return topicRepository.findById(id).map(topic -> {
			topic.setTopicCode(topicPatchDTO.getTopicCode());
			topic.setTopicDescription(topicPatchDTO.getTopicDescription());
			topic.setTopicName(topicPatchDTO.getTopicName());

			TopicPatchDTO returnDTO = topicMapper.topicToTopicPatchDTO(topicRepository.save(topic));

			return returnDTO;
		}).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public void addQuizToTopics(Long quizId, TopicIdListDTO topicIdListDTO) {

		List<Long> topicIds = topicIdListDTO.getIds();

		Quiz quiz = quizRepository.findById(quizId).orElseThrow(ResourceNotFoundException::new);

		for (Long topicId : topicIds) {
			Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
			topic.addQuiz(quiz);
		}
		quizRepository.save(quiz);
	}

}
