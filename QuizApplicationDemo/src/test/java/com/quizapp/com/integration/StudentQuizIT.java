package com.quizapp.com.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.quizapp.com.controllers.StudentQuizController;
import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.StudentScoreMapperImpl;
import com.quizapp.com.payload.StudentQuizDTO;
import com.quizapp.com.services.StudentQuizService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = StudentQuizController.class)
public class StudentQuizIT {

	@MockBean
	private StudentQuizService studentQuizService;

	@Autowired
	private MockMvc mockMvc;

	private StudentQuiz studentQuiz1;
	private StudentQuiz studentQuiz2;

	private StudentScoreMapperImpl studentScoreMapper;

	@BeforeEach
	void setUp() {
		studentScoreMapper = new StudentScoreMapperImpl();

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

		Student stud1 = new Student();
		stud1.setId(1L);
		stud1.setName("Aditya Shukla");
		stud1.setEducation("Btech");

		Student stud2 = new Student();
		stud2.setId(2L);
		stud2.setName("Rahul Singh");
		stud2.setEducation("Btech");

		StudentQuizScore st1 = new StudentQuizScore(stud1.getId(), 1L);
		StudentQuizScore st2 = new StudentQuizScore(stud2.getId(), 1L);

		studentQuiz1 = new StudentQuiz(st1, stud1, quiz1, 20);
		studentQuiz2 = new StudentQuiz(st2, stud2, quiz1, 45);

		quiz1.addStudent(studentQuiz1);
		quiz1.addStudent(studentQuiz2);

		topic.addQuiz(quiz1);
	}

	@DisplayName("It should update score of a Student in Quiz with HTTP.OK")
	@Test
	void itShouldUpdateStudentScore() throws Exception {
		studentQuiz1.setScore(45);

		StudentQuizDTO studentQuizDTO = studentScoreMapper.studentQuiztoStudentQuizDTO(studentQuiz1);

		when(studentQuizService.updateScoreOfaStudent(studentQuiz1.getQuiz().getId(), studentQuiz1.getStudent().getId(),
				45)).thenReturn(studentQuizDTO);

		mockMvc.perform(post("/api/topics/quizzes/result/updateStudentQuizScore").param("studId", "1")
				.param("quizId", "1").param("score", "45")).andExpect(status().isOk())
				.andExpect(jsonPath("$.score").value(45));

		verify(studentQuizService).updateScoreOfaStudent(1L, 1L, 45);

	}

	@DisplayName("It should fetch the leaderboard with status HTTP.OK")
	@Test
	void itShouldFetchTheLeaderboardWithStatusOK() throws Exception {

		List<StudentQuiz> tempList = new ArrayList<>();
		tempList.add(studentQuiz1);
		tempList.add(studentQuiz2);

		System.out.println(tempList);

		List<StudentQuizDTO> expectedListOfDtos = new ArrayList<>();

		for (StudentQuiz stq : tempList) {

			StudentQuizDTO temp = studentScoreMapper.studentQuiztoStudentQuizDTO(stq);
			expectedListOfDtos.add(temp);
		}

		when(studentQuizService.getLeaderboardForQuiz(1L)).thenReturn(expectedListOfDtos);

		MvcResult result = mockMvc.perform(get("/api/topics/quizzes/result/leaderboard/{id}", 1L)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		String response = result.getResponse().getContentAsString();
		
		System.out.println("Response = " +response);
		verify(studentQuizService).getLeaderboardForQuiz(1L);

	}

}
