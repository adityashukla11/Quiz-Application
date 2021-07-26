package com.quizapp.com.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.mapper.OptionMapperImpl;
import com.quizapp.com.mapper.QuestionMapperImpl;
import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.repository.QuestionRepository;
import com.quizapp.com.services.implementation.QuestionServiceImpl;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

	private QuestionService underTest;

	@Mock
	private QuestionRepository questionRepository;
	@InjectMocks
	private QuestionMapperImpl questionMapper;
	@InjectMocks
	private OptionMapperImpl optionMapper;

	@InjectMocks
	private Question ques1;

	@BeforeEach
	void setUp() {
		underTest = new QuestionServiceImpl(questionRepository, questionMapper, optionMapper);
		
		ques1.setId(1L);
		ques1.setDescription("Who is Sachin Tendulkar ?");
		Option op1 = new Option();
		op1.setId(1L);
		op1.setText("Boxer");
		ques1.getOptions().add(op1);

	}

	@DisplayName("It should get all questions")
	@Test
	void itShouldGetAllQuestions() {
		List<Question> expected = new ArrayList<>();
		expected.add(ques1);

		when(questionRepository.findAll()).thenReturn(expected);

		Set<QuestionDTO> actual = underTest.getAllQuestion();

		verify(questionRepository).findAll();

		assertThat(actual).hasSize(1);

	}

	@DisplayName("It should get all questions by keyword")
	@Test
	void itShouldGetQuestionByKeyword() {
		String keyword = "Sach";
		Set<Question> expected = new HashSet<>();
		expected.add(ques1);

		when(questionRepository.findAllByDescriptionContains(keyword)).thenReturn(expected);

		Set<QuestionDTO> actual = underTest.getQuestionByKeywords(keyword);

		verify(questionRepository).findAllByDescriptionContains(keyword);

		assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);

	}

	@DisplayName("It should get question by Id")
	@Test
	void itShouldGetAQuestionById() {

		Long id = 1L;
		
		when(questionRepository.findById(id)).thenReturn(Optional.of(ques1));
		
		QuestionDTO actual = underTest.getQuestion(id);
		
		verify(questionRepository).findById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(ques1);
	}

	@DisplayName("It should create a Question")
	@Test
	void itShouldCreateAQuestion() {
		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		when(questionRepository.save(Mockito.any())).thenReturn(ques1);

		QuestionDTO actual = underTest.createQuestion(questionDTO);

		ArgumentCaptor<Question> quesCaptor = ArgumentCaptor.forClass(Question.class);

		verify(questionRepository).save(quesCaptor.capture());

		Question capturedQuiz = quesCaptor.getValue();

		assertThat(capturedQuiz).usingRecursiveComparison().isEqualTo(ques1);

		assertThat(actual).usingRecursiveComparison().isEqualTo(ques1);

	}

	@DisplayName("It should update the questions !")
	@Test
	void itShouldUpdateAQuestion() {

		Long id = 1L;

		when(questionRepository.findById(id)).thenReturn(Optional.of(ques1));

		ques1.setDescription("Who is Virat Kohli ?");

		when(questionRepository.save(Mockito.any(Question.class))).thenReturn(ques1);

		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		QuestionDTO actual = underTest.updateQuestion(id, questionDTO);

		ArgumentCaptor<Question> quesCaptor = ArgumentCaptor.forClass(Question.class);

		verify(questionRepository).save(quesCaptor.capture());
		verify(questionRepository).findById(id);

		Question capturedQuiz = quesCaptor.getValue();

		assertThat(capturedQuiz).usingRecursiveComparison().isEqualTo(ques1);

		assertThat(actual).usingRecursiveComparison().isEqualTo(ques1);
	}

	@DisplayName("It should patch the question without loss in field. Checking by adding new option")
	@Test
	void itShouldPatchAQuestion() {

		Long id = 1L;

		when(questionRepository.findById(id)).thenReturn(Optional.of(ques1));

		Option op2 = new Option();
		op2.setId(2L);
		op2.setText("Cricketer");
		
		ques1.getOptions().add(op2);

		when(questionRepository.save(Mockito.any(Question.class))).thenReturn(ques1);

		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		QuestionDTO actual = underTest.patchQuestion(id, questionDTO);

		ArgumentCaptor<Question> quesCaptor = ArgumentCaptor.forClass(Question.class);

		verify(questionRepository).save(quesCaptor.capture());
		verify(questionRepository).findById(id);

		Question capturedQuiz = quesCaptor.getValue();

		assertThat(capturedQuiz).usingRecursiveComparison().isEqualTo(ques1);

		assertThat(actual).usingRecursiveComparison().isEqualTo(ques1);
		
		assertThat(actual.getOptions()).hasSize(2);
	}

	@DisplayName("It should delete a question")
	@Test
	void itShouldDeleteAQuestion() {
		
		Long id = 1L;
		
		underTest.deleteQuestion(id);
		
		verify(questionRepository).deleteById(id);
	}

}
