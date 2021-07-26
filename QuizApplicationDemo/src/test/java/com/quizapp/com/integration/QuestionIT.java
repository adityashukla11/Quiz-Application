package com.quizapp.com.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.quizapp.com.controllers.QuestionController;
import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.QuestionMapperImpl;
import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.services.QuestionService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = QuestionController.class)
public class QuestionIT {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private QuestionService questionService;

	private Question ques1;

	private QuestionMapperImpl questionMapper;

	@BeforeEach
	void setUp() {

		questionMapper = new QuestionMapperImpl();
		
		Topic topic = new Topic();

		topic.setId(1L);
		topic.setTopicCode("CH2021");
		topic.setTopicName("Chemistry");
		topic.setTopicDescription("An exiciting quiz on Chemistry");

		Quiz quiz1 = new Quiz();
		quiz1.setId(1L);
		quiz1.setQuizTitle("Maths Quiz");
		quiz1.setQuizDescription("This is a maths quiz");
		quiz1.setIsRunning(true);
		quiz1.setQuizCreateDateTime(LocalDateTime.of(2021, 6, 19, 0, 0, 0));

		ques1 = new Question();
		ques1.setId(1L);
		ques1.setDescription("Who is Sachin Tendulkar ?");

		Option op1 = new Option();
		op1.setId(1L);
		op1.setText("Boxer");
		ques1.getOptions().add(op1);

		Student stud1 = new Student();
		stud1.setId(1L);
		stud1.setName("Aditya Shukla");
		stud1.setEducation("Btech");
		StudentQuizScore st1 = new StudentQuizScore(stud1.getId(), 1L);

		StudentQuiz studentQuiz1 = new StudentQuiz(st1, stud1, quiz1, 20);

		quiz1.addStudent(studentQuiz1);
		quiz1.addQuestion(ques1);
		topic.addQuiz(quiz1);
	}

	@DisplayName("It should fetch all Question with HTTP.OK")
	@Test
	void itShouldGetAllQuestionWithStatus_OK() throws Exception {

		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		Set<QuestionDTO> expected = new HashSet<>();
		expected.add(questionDTO);

		when(questionService.getAllQuestion()).thenReturn(expected);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/questions").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.questions").exists())
				.andReturn();

		verify(questionService).getAllQuestion();
	}

	@DisplayName("It should fetch a Question with given Id and HTTP.OK")
	@Test
	void itShouldGetAQuestionByIdWithStatus_OK() throws Exception {

		Long id = 1L;
		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		when(questionService.getQuestion(id)).thenReturn(questionDTO);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/questions/{id}", 1L).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());

		verify(questionService).getQuestion(id);
	}

	@DisplayName("It should post a Question to server with HTTP.CREATED")
	@Test
	void itShouldCreateNewQuizWithStatus_CREATED() throws Exception {

		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		when(questionService.createQuestion(Mockito.any(QuestionDTO.class))).thenReturn(questionDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/questions").content(convertObjectToJson(questionDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<QuestionDTO> quesCaptor = ArgumentCaptor.forClass(QuestionDTO.class);

		verify(questionService).createQuestion(quesCaptor.capture());

		QuestionDTO capturedQuestionDTO = quesCaptor.getValue();

		assertThat(questionDTO).usingRecursiveComparison().isEqualTo(capturedQuestionDTO);

	}

	@DisplayName("It should update a Question to server with HTTP.OK")
	@Test
	void itShouldUpdateQuizWithStatus_OK() throws Exception {

		Long id = 1L;

		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		when(questionService.updateQuestion(Mockito.eq(id), Mockito.any(QuestionDTO.class))).thenReturn(questionDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.put("/api/questions/{id}", id).content(convertObjectToJson(questionDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<QuestionDTO> quesCaptor = ArgumentCaptor.forClass(QuestionDTO.class);

		verify(questionService).updateQuestion(Mockito.eq(id), quesCaptor.capture());

		QuestionDTO capturedQuestionDTO = quesCaptor.getValue();

		assertThat(questionDTO).usingRecursiveComparison().isEqualTo(capturedQuestionDTO);

	}

	@DisplayName("It should patch a Question to server with HTTP.OK")
	@Test
	void itShouldPatchQuizWithStatus_OK() throws Exception {

		Long id = 1L;

		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);

		when(questionService.patchQuestion(Mockito.eq(id), Mockito.any(QuestionDTO.class))).thenReturn(questionDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.patch("/api/questions/{id}", id)
						.content(convertObjectToJson(questionDTO)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<QuestionDTO> quesCaptor = ArgumentCaptor.forClass(QuestionDTO.class);

		verify(questionService).patchQuestion(Mockito.eq(id), quesCaptor.capture());

		QuestionDTO capturedQuestionDTO = quesCaptor.getValue();

		assertThat(questionDTO).usingRecursiveComparison().isEqualTo(capturedQuestionDTO);

	}

	@DisplayName("It should search with status code 200")
	@Test
	void itShouldSearchByKeyword() throws Exception {
		String keyword = "Math";
		QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(ques1);
		Set<QuestionDTO> questions = new HashSet<>();
		questions.add(questionDTO);

		when(questionService.getQuestionByKeywords(keyword)).thenReturn(questions);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/questions/searchByKeyword/{keyword}", keyword)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.questions[*].description").exists());

		verify(questionService).getQuestionByKeywords(keyword);

	}

	@DisplayName("It should delete a Question with HTTP.OK")
	@Test
	void itShouldDeleteAQuestion() throws Exception {
		Long id = 1L;

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/questions/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	public String convertObjectToJson(final Object obj) {
		try {
			ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
