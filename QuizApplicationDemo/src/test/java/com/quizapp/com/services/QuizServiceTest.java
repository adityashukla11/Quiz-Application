package com.quizapp.com.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.mapper.QuestionMapperImpl;
import com.quizapp.com.mapper.QuizMapperImpl;
import com.quizapp.com.mapper.StudentMapperImpl;
import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.payload.QuizDTO;
import com.quizapp.com.payload.QuizPatchDTO;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.StudentIdDTO;
import com.quizapp.com.payload.lists.QuestionIdListDTO;
import com.quizapp.com.payload.lists.QuestionIdListDTO;
import com.quizapp.com.repository.QuestionRepository;
import com.quizapp.com.repository.QuizRepository;
import com.quizapp.com.repository.StudentQuizRepository;
import com.quizapp.com.repository.StudentRepository;
import com.quizapp.com.services.implementation.QuizServiceImpl;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

	@Mock
	private QuizRepository quizRepository;
	@InjectMocks
	private QuizMapperImpl quizMapper;
	@InjectMocks
	private StudentMapperImpl studentMapper;
	@InjectMocks
	private QuestionMapperImpl questionMapper;
	@Mock
	private StudentQuizRepository studentQuizRepository;

	@Mock
	private QuestionRepository questionRepository;

	private QuizService underTest;

	@InjectMocks
	private Quiz quiz1;

	@InjectMocks
	private Quiz quiz2;

	@Mock
	private StudentRepository studentRepository;

	@BeforeEach
	void setUp() {

		underTest = new QuizServiceImpl(quizRepository, questionRepository, quizMapper, studentMapper, questionMapper,
				studentQuizRepository, studentRepository);

		quiz1.setId(1L);
		quiz1.setQuizTitle("Maths Quiz");
		quiz1.setQuizDescription("This is a maths quiz");
		quiz1.setIsRunning(true);
		quiz1.setQuizCreateDateTime(LocalDateTime.now());

		quiz2.setId(2L);
		quiz2.setQuizTitle("Bio Quiz");
		quiz2.setQuizDescription("This is a Bio quiz");
		quiz2.setIsRunning(false);
		quiz2.setQuizCreateDateTime(LocalDateTime.of(1999, 12, 28, 0, 0));

	}

	@DisplayName("This checks if all the Quiz can be fetched")
	@Test
	void itShouldGetAllQuiz() {
		List<Quiz> expected = new ArrayList<>();
		expected.add(quiz1);
		expected.add(quiz2);

		when(quizRepository.findAll()).thenReturn(expected);

		Set<QuizDTO> actual = underTest.getAllQuiz();

		verify(quizRepository).findAll();
		assertThat(actual).hasSize(2);
		assertThat(actual).extracting("id").containsExactlyInAnyOrder(quiz1.getId(), quiz2.getId());

	}

	@DisplayName("This checks if all the previous Quiz can be fetched")
	@Test
	void itShouldGetAllQPreviousQuiz() {
		Set<Quiz> expected = new HashSet<>();
		expected.add(quiz2);

		when(quizRepository.findByIsRunningEquals(false)).thenReturn(expected);

		Set<QuizDTO> actual = underTest.getAllpreviousQuiz();

		verify(quizRepository).findByIsRunningEquals(false);

		assertThat(actual).hasSize(1);
		assertThat(actual).extracting("id").contains(quiz2.getId());

	}

	@DisplayName("This checks if all the running Quiz can be fetched")
	@Test
	void itShouldGetAllRunningQuiz() {
		Set<Quiz> expected = new HashSet<>();
		expected.add(quiz1);

		when(quizRepository.findByIsRunningEquals(true)).thenReturn(expected);

		Set<QuizDTO> actual = underTest.getAllRunningQuiz();

		verify(quizRepository).findByIsRunningEquals(true);

		assertThat(actual).hasSize(1);
		assertThat(actual).extracting("id").contains(quiz1.getId());

	}

	@DisplayName("This checks whether all questions from a quiz can be fetched")
	@Test
	void itShouldGetAllQuestionFromQuiz() {
		Question ques1 = new Question();
		ques1.setId(1L);
		ques1.setDescription("What is 2 raised to power 2 ? ");
		quiz1.addQuestion(ques1);

		List<Question> questions = new ArrayList<>();
		questions.add(ques1);

		when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz1));

		Set<QuestionDTO> expected = quiz1.getQuestions().stream().map(questionMapper::questionToQuestionDTO)
				.collect(Collectors.toSet());
		Set<QuestionDTO> actual = underTest.getAllQuestion(1L);

		verify(quizRepository).findById(1L);

		assertThat(expected.size()).isEqualTo(actual.size());
		assertThat(expected).usingRecursiveFieldByFieldElementComparator().isEqualTo(actual);

	}

	@DisplayName("This checks all the quiz before a given date exists")
	@Test
	void itShouldGetAllQuizBeforeADate() {
		LocalDateTime dateTime = LocalDateTime.now();
		Set<Quiz> expected = new HashSet<>();
		expected.add(quiz2);

		when(quizRepository.findByQuizCreateDateTimeBefore(dateTime)).thenReturn(expected);

		Set<QuizDTO> actual = underTest.getAllQuizBeforeDate(dateTime);

		verify(quizRepository).findByQuizCreateDateTimeBefore(dateTime);

		assertThat(actual).hasSize(1);

		assertThat(actual).extracting("id").contains(quiz2.getId());
	}

	@DisplayName("It checks that a quiz is created")
	@Test
	void itShouldCreateAQuiz() {
		QuizPatchDTO quizPatchDTO = quizMapper.quizToQuizPatchDTO(quiz1);

		when(quizRepository.save(Mockito.any())).thenReturn(quiz1);

		QuizPatchDTO actual = underTest.createQuiz(quizPatchDTO);

		ArgumentCaptor<Quiz> quizCaptor = ArgumentCaptor.forClass(Quiz.class);

		verify(quizRepository).save(quizCaptor.capture());

		Quiz capturedQuiz = quizCaptor.getValue();

		assertThat(capturedQuiz).usingRecursiveComparison().isEqualTo(quiz1);

		assertThat(actual).usingRecursiveComparison().isEqualTo(quiz1);

	}

	@DisplayName("It checks that a quiz is updated")
	@Test
	void itShouldUpdateAQuiz() {

		Long id = 1L;

		when(quizRepository.findById(id)).thenReturn(Optional.of(quiz1));

		quiz1.setQuizTitle("Maths Wizard");

		QuizPatchDTO quizPatchDTO = quizMapper.quizToQuizPatchDTO(quiz1);

		when(quizRepository.save(Mockito.any())).thenReturn(quiz1);

		QuizPatchDTO actual = underTest.updateQuiz(id, quizPatchDTO);

		ArgumentCaptor<Quiz> quizCaptor = ArgumentCaptor.forClass(Quiz.class);

		verify(quizRepository).findById(id);
		verify(quizRepository).save(quizCaptor.capture());

		Quiz capturedQuiz = quizCaptor.getValue();

		assertThat(capturedQuiz).usingRecursiveComparison().isEqualTo(quiz1);

		assertThat(actual).usingRecursiveComparison().isEqualTo(quiz1);

	}

	@DisplayName("It checks that a quiz is patched without loss in field")
	@Test
	void itShouldPatchAQuiz() {

		Long id = 1L;

		when(quizRepository.findById(id)).thenReturn(Optional.of(quiz1));

		quiz1.setQuizTitle("Maths Wizard");

		QuizPatchDTO quizPatchDTO = quizMapper.quizToQuizPatchDTO(quiz1);

		when(quizRepository.save(Mockito.any())).thenReturn(quiz1);

		QuizPatchDTO actual = underTest.patchQuiz(id, quizPatchDTO);

		ArgumentCaptor<Quiz> quizCaptor = ArgumentCaptor.forClass(Quiz.class);

		verify(quizRepository).findById(id);
		verify(quizRepository).save(quizCaptor.capture());

		Quiz capturedQuiz = quizCaptor.getValue();

		assertThat(capturedQuiz).usingRecursiveComparison().isEqualTo(quiz1);

		assertThat(actual).usingRecursiveComparison().isEqualTo(quiz1);

	}

	@DisplayName("It should fetch quiz by id")
	@Test
	void itShouldGetQuizById() {
		Long id = 1L;

		when(quizRepository.findById(id)).thenReturn(Optional.of(quiz1));

		QuizDTO actual = underTest.getQuiz(id);

		verify(quizRepository).findById(id);
		assertThat(actual).usingRecursiveComparison().isEqualTo(quiz1);

	}

//	@DisplayName("It should delete a quiz")
//	@Test
//	void itShouldDeleteQuiz() {
//		Long id = 1L;
//		underTest.deleteQuiz(id);
//		verify(quizRepository).deleteById(id);
//	}

	@DisplayName("It should get all registered student from a quiz")
	@Test
	void itShouldGetAllRegisteredStudentFromQuiz() {
		Long id = 1L;

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

		StudentDTO studentDTO = studentMapper.studentToStudentDTO(stud1);
		Set<StudentDTO> expected = new HashSet<>();
		expected.add(studentDTO);

		StudentQuiz studentQuiz = quiz1.getStudentquiz().stream().filter(temp -> temp.getQuiz().getId() == 1)
				.findFirst().get();
		List<StudentQuiz> expectedListFromRepo = new ArrayList<>();
		expectedListFromRepo.add(studentQuiz);
		when(studentQuizRepository.findAllByIdQuizId(id)).thenReturn(expectedListFromRepo);

		Set<StudentDTO> actual = underTest.getAllRegisteredStudents(id);

		assertThat(actual).hasSize(1);

		assertThat(actual).extracting("id").contains(stud1.getId());

	}

	@DisplayName("It should add all Questions to a quiz")
	@Test
	void itShouldAddQuestionsToQuiz() {
		Question ques1 = new Question();
		ques1.setId(1L);
		ques1.setDescription("Who is Sachin Tendulkar ?");

		Option op1 = new Option();
		op1.setId(1L);
		op1.setText("Boxer");
		ques1.getOptions().add(op1);

		Question ques2 = new Question();
		ques2.setId(2L);
		ques2.setDescription("Who is CR7 ?");

		Option op2 = new Option();
		op2.setId(2L);
		op2.setText("Cricketer");
		ques2.getOptions().add(op2);

		when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz1));
		when(questionRepository.findById(1L)).thenReturn(Optional.of(ques1));
		when(questionRepository.findById(2L)).thenReturn(Optional.of(ques2));

		List<Long> questionIds = new ArrayList<>();
		questionIds.add(ques1.getId());
		questionIds.add(ques2.getId());

		QuestionIdListDTO quizIdListDTO = new QuestionIdListDTO(questionIds);

		underTest.addQuestionToQuiz(1L, quizIdListDTO);

		verify(quizRepository, times(1)).findById(1L);
		assertThat(quiz1.getQuestions()).containsExactlyInAnyOrder(ques1, ques2);
		assertThat(ques1.getQuiz()).isEqualTo(quiz1);
		assertThat(ques2.getQuiz()).isEqualTo(quiz1);

	}

	@DisplayName("It should add student to a Quiz")
	@Test
	void itShouldAddStudentToQuiz() {
		Student stud1 = new Student();
		stud1.setId(1L);
		stud1.setName("Aditya Shukla");
		stud1.setEducation("Btech");

		Long quizId = quiz1.getId();

		StudentIdDTO studentIdDTO = new StudentIdDTO(stud1.getId());
		
		when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz1));
		when(studentRepository.findById(stud1.getId())).thenReturn(Optional.of(stud1));

		underTest.addStudentToQuiz(quizId, studentIdDTO);

		verify(quizRepository).findById(quizId);
		verify(studentRepository).findById(stud1.getId());

		Student student = quiz1.getStudentquiz().stream().filter(studentQuiz -> studentQuiz.getQuiz().getId() == quizId
				&& studentQuiz.getStudent().getId() == stud1.getId()).findFirst().get().getStudent();
		
		Quiz quizMapped = stud1.getStudentquiz().stream().filter(studentQuiz -> studentQuiz.getQuiz().getId() == quizId
				&& studentQuiz.getStudent().getId() == stud1.getId()).findFirst().get().getQuiz();
		
		assertThat(quiz1.getStudentquiz()).extracting("student").contains(stud1);
		assertThat(student).isEqualTo(stud1);
		assertThat(quizMapped).isEqualTo(quiz1);
	}
}
