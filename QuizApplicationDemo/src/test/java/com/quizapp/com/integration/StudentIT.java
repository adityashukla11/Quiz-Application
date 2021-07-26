package com.quizapp.com.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.quizapp.com.controllers.StudentController;
import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.StudentMapperImpl;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.services.StudentService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = StudentController.class)
public class StudentIT {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StudentService studentService;
	
	private StudentMapperImpl studentMapper;
	
	private Student stud1;
	
	@BeforeEach
	void setUp()
	{
		studentMapper = new StudentMapperImpl();
		
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

		Question ques1 = new Question();
		ques1.setId(1L);
		ques1.setDescription("Who is Sachin Tendulkar ?");

		Option op1 = new Option();
		op1.setId(1L);
		op1.setText("Boxer");
		ques1.getOptions().add(op1);

		stud1 = new Student();
	    stud1 = new Student();
		stud1.setId(1L);
		stud1.setName("Aditya Shukla");
		stud1.setEducation("Btech");

		StudentQuizScore st1 = new StudentQuizScore(stud1.getId(), 1L);

		StudentQuiz studentQuiz1 = new StudentQuiz(st1, stud1, quiz1, 20);

		quiz1.addStudent(studentQuiz1);
		quiz1.addQuestion(ques1);
		topic.addQuiz(quiz1);

	}
	
	@DisplayName("It should get all students with HTTP.OK")
	@Test
	void itShouldGetAllStudent() throws Exception
	{
		StudentDTO studentDTO = studentMapper.studentToStudentDTO(stud1);
		Set<StudentDTO> expected = new HashSet<>();
		expected.add(studentDTO);
		when(studentService.getAllStudents()).thenReturn(expected);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/students").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.students").exists()).andReturn();

		String actualResponseBody = result.getResponse().getContentAsString();

		verify(studentService).getAllStudents();
	}
	
	@DisplayName("It should fetch a Student with given Id and HTTP.OK")
	@Test
	void itShouldGetAStudentByIdWithStatus_OK() throws Exception {

		Long id = 1L;
		StudentDTO studentDTO = studentMapper.studentToStudentDTO(stud1);

		when(studentService.getStudentById(id)).thenReturn(studentDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/students/{id}", id).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String expected = " {\"id\":1,\"name\":\"Aditya Shukla\",\"education\":\"Btech\"}";

		String actual = result.getResponse().getContentAsString();
		System.out.println("Response=" + actual);

		verify(studentService).getStudentById(id);

		assertThat(actual).isEqualToIgnoringWhitespace(expected);
	}
	
	@DisplayName("It should post a Student to server with HTTP.CREATED")
	@Test
	void itShouldCreateNewQuizWithStatus_CREATED() throws Exception {

		StudentDTO studentDTO = studentMapper.studentToStudentDTO(stud1);

		when(studentService.createNewStudent(Mockito.any(StudentDTO.class))).thenReturn(studentDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/students/").content(convertObjectToJson(studentDTO))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<StudentDTO> studentCaptor = ArgumentCaptor.forClass(StudentDTO.class);

		verify(studentService).createNewStudent(studentCaptor.capture());

		StudentDTO capturedStudentDTO = studentCaptor.getValue();

		assertThat(studentDTO).usingRecursiveComparison().isEqualTo(capturedStudentDTO);

	}
	@DisplayName("It should patch a Student to server with HTTP.OK")
	@Test
	void itShouldPatchAQuizWithStatus_OK() throws Exception {

		Long id = 1L;

		stud1.setEducation("BTECH IN CSE");

		StudentDTO studentDTO = studentMapper.studentToStudentDTO(stud1);

		when(studentService.patchStudent(Mockito.eq(id), Mockito.any(StudentDTO.class))).thenReturn(studentDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.patch("/api/students/{id}", id)
						.content(convertObjectToJson(studentDTO)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<StudentDTO> studentCaptor = ArgumentCaptor.forClass(StudentDTO.class);

		verify(studentService).patchStudent(Mockito.eq(id), studentCaptor.capture());

		StudentDTO capturedStudentDTO = studentCaptor.getValue();

		assertThat(studentDTO).usingRecursiveComparison().isEqualTo(capturedStudentDTO);

	}
	
	
	@DisplayName("It should update a Student to server with HTTP.OK")
	@Test
	void itShouldUpdateAQuizWithStatus_OK() throws Exception {

		Long id = 1L;

		stud1.setEducation("BTECH IN CSE");

		StudentDTO studentDTO = studentMapper.studentToStudentDTO(stud1);

		when(studentService.updateStudent(Mockito.eq(id), Mockito.any(StudentDTO.class))).thenReturn(studentDTO);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.put("/api/students/{id}", id)
						.content(convertObjectToJson(studentDTO)).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andReturn();

		ArgumentCaptor<StudentDTO> studentCaptor = ArgumentCaptor.forClass(StudentDTO.class);

		verify(studentService).updateStudent(Mockito.eq(id), studentCaptor.capture());

		StudentDTO capturedStudentDTO = studentCaptor.getValue();

		assertThat(studentDTO).usingRecursiveComparison().isEqualTo(capturedStudentDTO);

	}
	@DisplayName("It should delete a Student with given id and status_OK")
	@Test
	public void itShouldDeleteWithStatus_OK() throws Exception {
		Long id = 1L;
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/{id}", id)).andExpect(status().isOk());
		verify(studentService).deleteStudentById(id);
	}
	
	@DisplayName("A ResourceNotFoundException when Id not found and with HTTP.NOT_FOUND")
	@Test
	void itShouldThrowResourceNotFoundExceptionWithErrorCode_404() throws Exception
	{
		
		doThrow(new ResourceNotFoundException()).when(studentService).deleteStudentById(Mockito.any());
		
		 mockMvc.perform(delete("/api/students/{id}",1L)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isNotFound())
			      .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
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
