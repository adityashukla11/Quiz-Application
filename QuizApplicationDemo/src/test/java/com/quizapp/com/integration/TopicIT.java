package com.quizapp.com.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
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
import com.quizapp.com.controllers.TopicController;
import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.TopicMapperImpl;
import com.quizapp.com.matchers.ContainsKeyword;
import com.quizapp.com.payload.TopicDTO;
import com.quizapp.com.payload.TopicPatchDTO;
import com.quizapp.com.payload.lists.TopicIdListDTO;
import com.quizapp.com.services.TopicService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TopicController.class)
public class TopicIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TopicService topicService;

	private Topic topic;

	private TopicMapperImpl topicMapper;

	@BeforeEach
	void setUp() {

		topicMapper = new TopicMapperImpl();
		topic = new Topic();

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

		Question ques1 = new Question();
		ques1.setId(1L);
		ques1.setDescription("Who is Sachin Tendulkar ?");

		Option op1 = new Option();
		op1.setId(1L);
		op1.setText("Boxer");
		ques1.getOptions().add(op1);

		Student stud1 = new Student();
		stud1.setName("Aditya Shukla");
		stud1.setEducation("Btech");


		StudentQuizScore st1 = new StudentQuizScore(stud1.getId(), 1L);

		StudentQuiz studentQuiz1 = new StudentQuiz(st1, stud1, quiz1, 20);

		quiz1.addStudent(studentQuiz1);
		
		quiz1.addQuestion(ques1);
		topic.addQuiz(quiz1);
	}

	@DisplayName("It should fetch all Topics with HTTP.OK")
	@Test
	void itShouldGetAllTopicsWithStatus_OK() throws Exception {

		TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic);
		Set<TopicDTO> expected = new HashSet<>();
		expected.add(topicDTO);
		when(topicService.getAllTopics()).thenReturn(expected);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/topics").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.topics").exists()).andReturn();

		String actualResponseBody = result.getResponse().getContentAsString();
		verify(topicService).getAllTopics();
	}

	@DisplayName("It should fetch a topic with id with HTTP.OK")
	@Test
	void itShouldFetchATopicWithIdWithStatus_OK() throws Exception {
		Long id = 1L;
		TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic);

		when(topicService.getTopicById(id)).thenReturn(topicDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/topics/{id}", 1L).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String expected = "{\"id\":1,\"topicCode\":\"CH2021\",\"topicName\":\"Chemistry\",\"topicDescription\":\"An exiciting quiz on Chemistry\",\"quizzes\":[{\"id\":1,\"quizTitle\":\"Maths Quiz\",\"quizDescription\":\"This is a maths quiz\",\"questions\":[{\"id\":1,\"description\":\"Who is Sachin Tendulkar ?\",\"options\":[{\"id\":1,\"text\":\"Boxer\"}]}],\"topics\":[{\"id\":1,\"topicCode\":\"CH2021\",\"topicName\":\"Chemistry\",\"topicDescription\":\"An exiciting quiz on Chemistry\"}],\"quizCreateDateTime\":\"2021-06-19T00:00:00\",\"isRunning\":true}]}";

		String actual = result.getResponse().getContentAsString();
		System.out.println("Response=" + actual);
		verify(topicService).getTopicById(id);

		assertThat(actual).isEqualToIgnoringWhitespace(expected);
	}

	@DisplayName("It should post a topic to server with HTTP.CREATED")
	@Test
	void itShouldCreateNewTopicWithStatus_CREATED() throws Exception {
		TopicPatchDTO topicPatchDTO = topicMapper.topicToTopicPatchDTO(topic);

		System.out.println(topicPatchDTO.getTopicCode() + " " + topicPatchDTO.getId());

		when(topicService.createNewTopic(Mockito.any(TopicPatchDTO.class))).thenReturn(topicPatchDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/topics").content(convertObjectToJson(topicPatchDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<TopicPatchDTO> topicCaptor = ArgumentCaptor.forClass(TopicPatchDTO.class);

		verify(topicService).createNewTopic(topicCaptor.capture());

		TopicPatchDTO capturedTopicPatchDTO = topicCaptor.getValue();

		assertThat(topicPatchDTO).usingRecursiveComparison().isEqualTo(capturedTopicPatchDTO);

	}

	@DisplayName("It should update a topic with HTTP.OK")
	@Test
	void itShouldUpdateTopicWithStatus_OK() throws Exception {
		Long id = 1L;

		TopicPatchDTO topicPatchDTO = topicMapper.topicToTopicPatchDTO(topic);

		when(topicService.updateTopic(Mockito.anyLong(), Mockito.any(TopicPatchDTO.class))).thenReturn(topicPatchDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.put("/api/topics/{id}", 1L).content(convertObjectToJson(topicPatchDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<TopicPatchDTO> topicCaptor = ArgumentCaptor.forClass(TopicPatchDTO.class);
		ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

		verify(topicService).updateTopic(idCaptor.capture(), topicCaptor.capture());

		TopicPatchDTO capturedTopicPatchDTO = topicCaptor.getValue();
		Long capturedId = idCaptor.getValue();
		assertThat(capturedId).isEqualTo(id);
		assertThat(topicPatchDTO).usingRecursiveComparison().isEqualTo(capturedTopicPatchDTO);
	}

	@DisplayName("It should patch a topic with HTTP.OK")
	@Test
	void itShouldPatchTopicWithStatus_OK() throws Exception {
		Long id = 1L;

		topic.setTopicDescription("An exciting quiz on Chem");
		TopicPatchDTO topicPatchDTO = topicMapper.topicToTopicPatchDTO(topic);

		when(topicService.patchTopic(Mockito.anyLong(), Mockito.any(TopicPatchDTO.class))).thenReturn(topicPatchDTO);

		MvcResult result = mockMvc
				.perform(
						MockMvcRequestBuilders.patch("/api/topics/{id}", 1L).content(convertObjectToJson(topicPatchDTO))
								.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.topicCode").exists())
				.andExpect(jsonPath("$.topicDescription").value("An exciting quiz on Chem")).andReturn();

		ArgumentCaptor<TopicPatchDTO> topicCaptor = ArgumentCaptor.forClass(TopicPatchDTO.class);
		ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

		verify(topicService).patchTopic(idCaptor.capture(), topicCaptor.capture());

		TopicPatchDTO capturedTopicPatchDTO = topicCaptor.getValue();
		Long capturedId = idCaptor.getValue();
		assertThat(capturedId).isEqualTo(id);
		assertThat(topicPatchDTO).usingRecursiveComparison().isEqualTo(capturedTopicPatchDTO);
	}

	@DisplayName("It should delete a Topic with given")
	@Test
	public void itShouldDeleteWithStatus_OK() throws Exception {
		Long id = 1L;
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/topics/{id}", 1)).andExpect(status().isOk());
		verify(topicService).deleteTopicById(1L);
	}

	@DisplayName("It should add all topics to a Quiz")
	@Test
	public void itShouldAddAllTopicsToQuizWithStatus_OK() throws Exception {

		Topic topic3 = new Topic();
		topic3.setId(3L);
		topic3.setTopicCode("RNDM21");
		topic3.setTopicName("Random Topics");

		Quiz quiz2 = new Quiz();
		quiz2.setId(2L);
		quiz2.setQuizTitle("Random Quiz");
		quiz2.setQuizDescription("This is a random quiz");
		quiz2.setIsRunning(true);
		quiz2.setQuizCreateDateTime(LocalDateTime.of(2021, 6, 19, 0, 0, 0));

		List<Long> ids = new ArrayList<>();

		ids.add(topic3.getId());
		ids.add(topic.getId());

		TopicIdListDTO topicIdListDTO = new TopicIdListDTO(ids);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/topics/addQuizToTopics").param("quizId", "2")
				.content(convertObjectToJson(topicIdListDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

		ArgumentCaptor<TopicIdListDTO> argumentCaptor = ArgumentCaptor.forClass(TopicIdListDTO.class);

		verify(topicService).addQuizToTopics(Mockito.eq(2L), argumentCaptor.capture());

		TopicIdListDTO actual = argumentCaptor.getValue();

		assertThat(actual).usingRecursiveComparison().isEqualTo(topicIdListDTO);
	}

	@DisplayName("It searches a topic by Topic Name with status code HTTP.OK")
	@Test
	void itSearchesTopicsByTopicNameWithStatus_Ok() throws Exception {
		String keyword = "Chem";
		TopicDTO topicDTO = topicMapper.topicToTopicDTO(topic);
		
		Set<TopicDTO> expected = new HashSet<>();
		expected.add(topicDTO);
		when(topicService.searchTopicByName(keyword)).thenReturn(expected);
		mockMvc.perform(get("/api/topics/searchByName/{keyword}", keyword).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.topics[0].topicName",(Matchers.containsString(keyword)))).andExpect(jsonPath("$.topics[*].topicName[*]", Matchers.everyItem(Matchers.startsWith(keyword))));

		verify(topicService).searchTopicByName(keyword);
	}

	public String convertObjectToJson(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
