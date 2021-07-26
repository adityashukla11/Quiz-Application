package com.quizapp.com.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.quizapp.com.controllers.QuizController;
import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.QuizMapperImpl;
import com.quizapp.com.mapper.StudentMapperImpl;
import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.payload.QuizDTO;
import com.quizapp.com.payload.QuizPatchDTO;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.StudentIdDTO;
import com.quizapp.com.payload.lists.QuestionIdListDTO;
import com.quizapp.com.services.QuizService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(QuizController.class)
public class QuizIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private QuizService quizService;

	private Quiz quiz1;

	private QuizMapperImpl quizMapper;

	private StudentMapperImpl studentMapper;

	@BeforeEach
	void setUp() {
		quizMapper = new QuizMapperImpl();
		studentMapper = new StudentMapperImpl();
		Topic topic = new Topic();

		topic.setId(1L);
		topic.setTopicCode("CH2021");
		topic.setTopicName("Chemistry");
		topic.setTopicDescription("An exiciting quiz on Chemistry");

		quiz1 = new Quiz();
		quiz1.setId(1L);
		quiz1.setQuizTitle("Maths Quiz");
		quiz1.setQuizDescription("This is a maths quiz");
		quiz1.setIsRunning(true);
		quiz1.setQuizCreateDateTime(LocalDateTime.of(2021, 6, 19, 0, 0, 0));

		Question ques1 = new Question();
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

	@DisplayName("It should fetch all quizzes with HTTP.OK")
	@Test
	void itShouldGetAllQuizzesWithStatus_OK() throws Exception {

		QuizDTO quizDTO = quizMapper.quizToQuizDTO(quiz1);
		Set<QuizDTO> expected = new HashSet<>();
		expected.add(quizDTO);
		when(quizService.getAllQuiz()).thenReturn(expected);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/topics/quizzes").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.quizzes").exists()).andReturn();

		String actualResponseBody = result.getResponse().getContentAsString();

		verify(quizService).getAllQuiz();
	}

	@DisplayName("It should fetch a Quiz with given Id and HTTP.OK")
	@Test
	void itShouldGetAQuizByIdWithStatus_OK() throws Exception {

		Long id = 1L;
		QuizDTO quizDTO = quizMapper.quizToQuizDTO(quiz1);

		when(quizService.getQuiz(id)).thenReturn(quizDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/topics/quizzes/{id}", 1L).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String expected = "{\"id\":1,\"quizTitle\":\"Maths Quiz\",\"quizDescription\":\"This is a maths quiz\",\"questions\":[{\"id\":1,\"description\":\"Who is Sachin Tendulkar ?\",\"options\":[{\"id\":1,\"text\":\"Boxer\"}]}],\"topics\":[{\"id\":1,\"topicCode\":\"CH2021\",\"topicName\":\"Chemistry\",\"topicDescription\":\"An exiciting quiz on Chemistry\"}],\"quizCreateDateTime\":\"2021-06-19T00:00:00\",\"isRunning\":true}";

		String actual = result.getResponse().getContentAsString();
		System.out.println("Response=" + actual);

		verify(quizService).getQuiz(id);

		assertThat(actual).isEqualToIgnoringWhitespace(expected);
	}

	@DisplayName("It should post a Quiz to server with HTTP.CREATED")
	@Test
	void itShouldCreateNewQuizWithStatus_CREATED() throws Exception {

		QuizPatchDTO quizPatchDTO = quizMapper.quizToQuizPatchDTO(quiz1);

		when(quizService.createQuiz(Mockito.any(QuizPatchDTO.class))).thenReturn(quizPatchDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/topics/quizzes").content(convertObjectToJson(quizPatchDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<QuizPatchDTO> quizCaptor = ArgumentCaptor.forClass(QuizPatchDTO.class);

		verify(quizService).createQuiz(quizCaptor.capture());

		QuizPatchDTO capturedQuizPatchDTO = quizCaptor.getValue();

		assertThat(quizPatchDTO).usingRecursiveComparison().isEqualTo(capturedQuizPatchDTO);

	}

	@DisplayName("It should update a quiz with HTTP.OK")
	@Test
	void itShouldUpdateAQuizWithStatus_isOk() throws Exception {

		Long id = 1L;

		quiz1.setQuizTitle("Maths Mania");

		QuizPatchDTO quizPatchDTO = quizMapper.quizToQuizPatchDTO(quiz1);

		when(quizService.updateQuiz(Mockito.eq(id), Mockito.any(QuizPatchDTO.class))).thenReturn(quizPatchDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.put("/api/topics/quizzes/{id}", id)
						.content(convertObjectToJson(quizPatchDTO)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<QuizPatchDTO> quizCaptor = ArgumentCaptor.forClass(QuizPatchDTO.class);

		verify(quizService).updateQuiz(Mockito.eq(id), quizCaptor.capture());

		QuizPatchDTO capturedQuizPatchDTO = quizCaptor.getValue();

		assertThat(quizPatchDTO).usingRecursiveComparison().isEqualTo(capturedQuizPatchDTO);
	}

	@DisplayName("It should patch a Quiz with status code HTTP.200")
	@Test
	void itShouldPatchQuizWithStatus_200() throws Exception {

		Long id = 1L;

		quiz1.setQuizTitle("Maths Mania");

		QuizPatchDTO quizPatchDTO = quizMapper.quizToQuizPatchDTO(quiz1);

		when(quizService.patchQuiz(Mockito.eq(id), Mockito.any(QuizPatchDTO.class))).thenReturn(quizPatchDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.patch("/api/topics/quizzes/{id}", id)
						.content(convertObjectToJson(quizPatchDTO)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<QuizPatchDTO> quizCaptor = ArgumentCaptor.forClass(QuizPatchDTO.class);

		verify(quizService).patchQuiz(Mockito.eq(id), quizCaptor.capture());

		QuizPatchDTO capturedQuizPatchDTO = quizCaptor.getValue();

		assertThat(quizPatchDTO).usingRecursiveComparison().isEqualTo(capturedQuizPatchDTO);

	}

	@DisplayName("It should delete a Quiz with given id and status_OK")
	@Test
	public void itShouldDeleteWithStatus_OK() throws Exception {
		Long id = 1L;
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/topics/quizzes/{id}", id)).andExpect(status().isOk());
		verify(quizService).deleteQuiz(id);
	}

	@DisplayName("Get all question in a quiz with given id and status HTTP.OK")
	@Test
	public void itShouldGetAllQuestionInAQuiz() throws Exception {
		QuizDTO quizDTO = quizMapper.quizToQuizDTO(quiz1);

		Set<QuestionDTO> questionDTOs = quizDTO.getQuestions();
		Long id = 1L;

		when(quizService.getAllQuestion(id)).thenReturn(questionDTOs);

		MvcResult result = mockMvc
				.perform(get("/api/topics/quizzes/getAllQuestions/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.questions").exists()).andReturn();

		verify(quizService).getAllQuestion(id);

	}

	@DisplayName("It should get all running quiz with status code OK")
	@Test
	public void itShouldFetchAllRunningQuizWithStatus_Ok() throws Exception {
		QuizDTO quizDTO = quizMapper.quizToQuizDTO(quiz1);
		Set<QuizDTO> expected = new HashSet<QuizDTO>();
		expected.add(quizDTO);
		when(quizService.getAllRunningQuiz()).thenReturn(expected);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/topics/quizzes/allRunningQuiz")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.quizzes").exists()).andReturn();

		verify(quizService).getAllRunningQuiz();
	}

	@DisplayName("It should get all previous quiz with status code OK")
	@Test
	public void itShouldFetchAllPreviousQuizWithStatus_Ok() throws Exception {
		quiz1.setIsRunning(false);
		QuizDTO quizDTO = quizMapper.quizToQuizDTO(quiz1);
		Set<QuizDTO> expected = new HashSet<QuizDTO>();
		expected.add(quizDTO);
		when(quizService.getAllpreviousQuiz()).thenReturn(expected);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/topics/quizzes/allPreviousQuiz")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.quizzes").exists()).andReturn();

		verify(quizService).getAllpreviousQuiz();
	}

	@DisplayName("It should get all registered student of a quiz with status OK")
	@Test
	public void itShouldGetAllRegisteredStudentWithStatus_OK() throws Exception {

		Long id = 1L;

		Student student = quiz1.getStudentquiz().stream().filter(temp -> temp.getQuiz().getId() == id).findFirst().get()
				.getStudent();

		StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);

		Set<StudentDTO> expected = new HashSet<>();
		expected.add(studentDTO);

		when(quizService.getAllRegisteredStudents(id)).thenReturn(expected);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/topics/quizzes/getAllRegisteredStudent/{id}", id)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.students[0].id").exists());
		verify(quizService).getAllRegisteredStudents(id);
	}

	@DisplayName("It should get all quiz before a certain date with status with HTTP code OK")
	@Test
	public void itShouldGetAllQuizBeforeACertainDateWithStatus_OK() throws Exception {
		LocalDateTime dateTime = LocalDateTime.of(2021, 12, 12, 0, 0, 0);

		mockMvc.perform(
				get("/api/topics/quizzes/allQuizBefore").param("date", "2021-12-12").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());

		verify(quizService).getAllQuizBeforeDate(dateTime);
	}

	@DisplayName("It should add all Questions to a Quiz")
	@Test
	public void itShouldAddAllQuestionsToQuizWithStatus_OK() throws Exception {

		Question ques3 = new Question();
		ques3.setId(3L);
		ques3.setDescription("Who is President of India ?");

		Option op3 = new Option();
		op3.setId(3L);
		op3.setText("Ram Nath Kovind");
		ques3.getOptions().add(op3);

		Question ques2 = new Question();
		ques2.setId(2L);
		ques2.setDescription("Who is CR7 ?");

		Option op2 = new Option();
		op2.setId(2L);
		op2.setText("Cricketer");
		ques2.getOptions().add(op2);

		List<Long> ids = new ArrayList<>();

		ids.add(ques2.getId());
		ids.add(ques3.getId());

		QuestionIdListDTO questionIdListDTO = new QuestionIdListDTO(ids);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/topics/quizzes/addQuestionsToQuiz").param("quizId", "1")
				.content(convertObjectToJson(questionIdListDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

		ArgumentCaptor<QuestionIdListDTO> argumentCaptor = ArgumentCaptor.forClass(QuestionIdListDTO.class);

		verify(quizService).addQuestionToQuiz(Mockito.eq(1L), argumentCaptor.capture());

		QuestionIdListDTO actual = argumentCaptor.getValue();

		assertThat(actual).usingRecursiveComparison().isEqualTo(questionIdListDTO);
	}

	@DisplayName("It should add Student to a Quiz with status code HTTP.OK")
	@Test
	void itShouldAddStudentToAQuizWithStatus_Ok() throws Exception {
		Long quizId = 1L;

		Student st1 = new Student();
		st1.setId(3L);
		st1.setName("Ankur");
		st1.setEducation("BSC");

		mockMvc.perform(post("/api/topics/quizzes/addStudentToQuiz").param("quizId", "3").content(convertObjectToJson(new StudentIdDTO(3L))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		ArgumentCaptor<StudentIdDTO> captor = ArgumentCaptor.forClass(StudentIdDTO.class);

		verify(quizService).addStudentToQuiz(Mockito.eq(3L), captor.capture());

		System.out.println(st1.getStudentquiz());
		assertThat(captor.getValue().getStudId()).isEqualTo(3L);
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
