package com.quizapp.com.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.mapper.StudentScoreMapperImpl;
import com.quizapp.com.payload.StudentQuizDTO;
import com.quizapp.com.repository.QuizRepository;
import com.quizapp.com.repository.StudentQuizRepository;
import com.quizapp.com.repository.StudentRepository;
import com.quizapp.com.services.implementation.StudentQuizServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentQuizServiceTest {

	@Mock
	private StudentQuizRepository studentQuizRepository;

	private StudentScoreMapperImpl studentScoreMapper;

	@Mock
	private QuizRepository quizRepository;

	@Mock
	private StudentRepository studentRepository;

	private StudentQuizService underTest;

	private Quiz quiz1;

	@BeforeEach
	public void setUp() {
		studentScoreMapper = new StudentScoreMapperImpl();
		underTest = new StudentQuizServiceImpl(studentQuizRepository, studentScoreMapper, quizRepository,
				studentRepository);
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

	}

	@DisplayName("A Student info can be fetched with Student id and Quiz id")
	@Test
	void itShouldFetchQuizInfoOfAStudent() {

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

		Long studId = 1L;
		Long quizId = 1L;

		when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz1));
		StudentQuiz expected = quiz1.getStudentquiz().stream()
				.filter(temp -> temp.getQuiz().getId() == studId && temp.getStudent().getId() == quizId).findFirst()
				.get();
		StudentQuizDTO actual = underTest.findByQuizIdAndStudentId(quizId, studId);
		StudentQuizDTO expectedQuizDTO = studentScoreMapper.studentQuiztoStudentQuizDTO(expected);
		assertThat(expectedQuizDTO).usingRecursiveComparison().isEqualTo(actual);

	}

	@DisplayName("It should update the score of a Student in a Quiz")
	@Test
	void itShouldUpdateQuizScore() {
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

		Long studId = 1L;
		Long quizId = 1L;

		when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz1));

		StudentQuiz expected = quiz1.getStudentquiz().stream()
				.filter(temp -> temp.getQuiz().getId() == quizId && temp.getStudent().getId() == studId).findFirst()
				.get();

		expected.setScore(10);

		when(studentQuizRepository.save(Mockito.any())).thenReturn(expected);

		StudentQuizDTO actual = underTest.updateScoreOfaStudent(studId, quizId, 10);

		StudentQuizDTO expectedQuizDTO = studentScoreMapper.studentQuiztoStudentQuizDTO(expected);

		assertThat(expectedQuizDTO).usingRecursiveComparison().isEqualTo(actual);
	}

	@DisplayName("It should get the Leaderboard")
	@Test
	void itShouldGetLeaderBoard() {

		Student stud1 = new Student();
		stud1.setId(1L);
		stud1.setName("Aditya Shukla");
		stud1.setEducation("Btech");

		Student stud2 = new Student();
		stud2.setId(2L);
		stud2.setName("Rahul Singh");
		stud2.setEducation("Btech");

		StudentQuizScore st1 = new StudentQuizScore(stud1.getId(), quiz1.getId());
		StudentQuizScore st2 = new StudentQuizScore(stud2.getId(), quiz1.getId());

		StudentQuiz studentQuiz1 = new StudentQuiz(st1, stud1, quiz1, 20);
		StudentQuiz studentQuiz2 = new StudentQuiz(st2, stud2, quiz1, 45);

		quiz1.addStudent(studentQuiz1);
		quiz1.addStudent(studentQuiz2);

		List<StudentQuiz> expected = quiz1.getStudentquiz().stream().collect(Collectors.toList());
		Long id = 1L;

		when(studentQuizRepository.findAllByIdQuizIdOrderByScoreDesc(id)).thenReturn(expected);

		List<StudentQuizDTO> actual = underTest.getLeaderboardForQuiz(quiz1.getId());

		assertThat(actual).hasSize(2);
	}
}
