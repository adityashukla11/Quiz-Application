package com.quizapp.com.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;

@DataJpaTest
public class StudentQuizRepositoryTest {

	@Autowired
	private StudentQuizRepository underTest;

	@Autowired
	private QuizRepository quizRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private StudentRepository studentRepository;
	
	private StudentQuiz studentQuiz1;
	private StudentQuiz studentQuiz2;

	@BeforeEach
	public void setUp() {
		
		Topic topic = new Topic();

		topic.setTopicCode("CH2021");
		topic.setTopicName("Chemistry");
		topic.setTopicDescription("An exiciting quiz on Chemistry");

		Quiz quiz1 = new Quiz();
		quiz1.setQuizTitle("Maths Quiz");
		quiz1.setQuizDescription("This is a maths quiz");
		quiz1.setIsRunning(true);
		quiz1.setQuizCreateDateTime(LocalDateTime.of(2021, 6, 19, 0, 0, 0));

		Question ques1 = new Question();
		ques1.setDescription("Who is Sachin Tendulkar ?");

		Option op1 = new Option();
		op1.setText("Boxer");
		ques1.getOptions().add(op1);

		Student stud1 = new Student();
		stud1.setName("Aditya Shukla");
		stud1.setEducation("Btech");

		Student stud2 = new Student();
		stud2.setName("Rahul Singh");
		stud2.setEducation("Btech");

		studentRepository.save(stud1);
		studentRepository.save(stud2);
		
		quiz1.addQuestion(ques1);

		topic.addQuiz(quiz1);

		StudentQuizScore st1 = new StudentQuizScore(stud1.getId(), quiz1.getId());
		StudentQuizScore st2 = new StudentQuizScore(stud2.getId(), quiz1.getId());

		studentQuiz1 = new StudentQuiz(st1, stud1, quiz1, 20);
		studentQuiz2 = new StudentQuiz(st2, stud2, quiz1, 45);
		
		
		quizRepository.save(quiz1);
		
		quiz1.addStudent(studentQuiz1);
		quiz1.addStudent(studentQuiz2);
		
		
		underTest.save(studentQuiz1);
		underTest.save(studentQuiz2);
		
		

	}
	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}
	
	@DisplayName("It checks whether leaderboard is fetched")
	@Test
	void itGetsTheLeaderboard() {

		List<StudentQuiz> actual = underTest.findAllByIdQuizIdOrderByScoreDesc(studentQuiz1.getQuiz().getId());

		assertThat(actual).extracting("student.id").containsExactly(studentQuiz2.getStudent().getId(),
				studentQuiz1.getStudent().getId());
	}

}
