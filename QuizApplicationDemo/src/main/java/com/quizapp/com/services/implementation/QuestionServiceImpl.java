package com.quizapp.com.services.implementation;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Question;
import com.quizapp.com.mapper.OptionMapper;
import com.quizapp.com.mapper.QuestionMapper;
import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.repository.QuestionRepository;
import com.quizapp.com.services.QuestionService;


@Service
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;
	private final QuestionMapper questionMapper;
	private final OptionMapper optionMapper;

	public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper,
			OptionMapper optionMapper) {
		this.questionRepository = questionRepository;
		this.questionMapper = questionMapper;
		this.optionMapper = optionMapper;
	}

	@Override
	public Set<QuestionDTO> getAllQuestion() {
		return questionRepository.findAll().stream().map(questionMapper::questionToQuestionDTO)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<QuestionDTO> getQuestionByKeywords(String keywords) {
		return questionRepository.findAllByDescriptionContains(keywords).stream()
				.map(questionMapper::questionToQuestionDTO).collect(Collectors.toSet());
	}

	@Override
	public QuestionDTO getQuestion(Long id) {
		return questionRepository.findById(id).map(questionMapper::questionToQuestionDTO).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public QuestionDTO createQuestion(QuestionDTO questionDTO) {
		return saveAndReturnDTO(questionMapper.questionDTOtoQuestion(questionDTO));
	}

	@Override
	public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
		return questionRepository.findById(id).map(question -> {
			question.setDescription(questionDTO.getDescription());
			question.setOptions(
					questionDTO.getOptions().stream().map(optionMapper::optionDTOtoOption).collect(Collectors.toSet()));
			QuestionDTO returnDTO = questionMapper.questionToQuestionDTO(questionRepository.save(question));
			return returnDTO;
		}).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public QuestionDTO patchQuestion(Long id, QuestionDTO questionDTO) {
		return questionRepository.findById(id).map(question -> {

			if (questionDTO.getDescription() != null)
				question.setDescription(questionDTO.getDescription());

			if (questionDTO.getOptions() != null)
				question.setOptions(questionDTO.getOptions().stream().map(optionMapper::optionDTOtoOption)
						.collect(Collectors.toSet()));

			QuestionDTO returnDTO = questionMapper.questionToQuestionDTO(questionRepository.save(question));

			return returnDTO;

		}).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public void deleteQuestion(Long id) {
		questionRepository.deleteById(id);

	}

	private QuestionDTO saveAndReturnDTO(Question question) {
		Question savedQuestion = questionRepository.save(question);
		return questionMapper.questionToQuestionDTO(savedQuestion);
	}

}
