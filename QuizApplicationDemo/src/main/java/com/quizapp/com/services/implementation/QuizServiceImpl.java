package com.quizapp.com.services.implementation;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.mapper.QuestionMapper;
import com.quizapp.com.mapper.QuizMapper;
import com.quizapp.com.mapper.StudentMapper;
import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.payload.QuizDTO;
import com.quizapp.com.payload.QuizPatchDTO;
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.StudentIdDTO;
import com.quizapp.com.payload.lists.QuestionIdListDTO;
import com.quizapp.com.repository.QuestionRepository;
import com.quizapp.com.repository.QuizRepository;
import com.quizapp.com.repository.StudentQuizRepository;
import com.quizapp.com.repository.StudentRepository;
import com.quizapp.com.services.QuizService;

@Service
public class QuizServiceImpl implements QuizService {

	private final QuizRepository quizRepository;
	private final QuestionRepository questionRepository;
	private final QuizMapper quizMapper;
	private final StudentMapper studentMapper;
	private final QuestionMapper questionMapper;
	private final StudentQuizRepository studentQuizRepository;
	private final StudentRepository studentRepository;


	private final Logger LOGGER = LoggerFactory.getLogger(QuizServiceImpl.class);
	

	

	public QuizServiceImpl(QuizRepository quizRepository, QuestionRepository questionRepository, QuizMapper quizMapper,
			StudentMapper studentMapper, QuestionMapper questionMapper, StudentQuizRepository studentQuizRepository,
			StudentRepository studentRepository) {
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.quizMapper = quizMapper;
		this.studentMapper = studentMapper;
		this.questionMapper = questionMapper;
		this.studentQuizRepository = studentQuizRepository;
		this.studentRepository = studentRepository;
	}

	@Override
	public Set<QuizDTO> getAllRunningQuiz() {

		return quizRepository.findByIsRunningEquals(true).stream().map(quizMapper::quizToQuizDTO)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<QuizDTO> getAllpreviousQuiz() {

		return quizRepository.findByIsRunningEquals(false).stream().map(quizMapper::quizToQuizDTO)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<StudentDTO> getAllRegisteredStudents(Long id) {

		Set<Student> students = studentQuizRepository.findAllByIdQuizId(id).stream().map(studentQuiz -> {
			return studentQuiz.getStudent();
		}).collect(Collectors.toSet());

		return students.stream().map(studentMapper::studentToStudentDTO).collect(Collectors.toSet());
	}

	@Override
	public Set<QuestionDTO> getAllQuestion(Long id) {
		Set<Question> questionDTOs = quizRepository.findById(id).get().getQuestions();
		return questionDTOs.stream().map(questionMapper::questionToQuestionDTO).collect(Collectors.toSet());
	}

	@Override
	public Set<QuizDTO> getAllQuizBeforeDate(LocalDateTime dateTime) {
		return quizRepository.findByQuizCreateDateTimeBefore(dateTime).stream().map(quizMapper::quizToQuizDTO)
				.collect(Collectors.toSet());
	}

	@Override
	public QuizPatchDTO createQuiz(QuizPatchDTO quiz) {
		return saveAndReturnDTO(quizMapper.quizPatchDTOToQuiz(quiz));
	}

	@Override
	public QuizDTO getQuiz(Long id) {
		return quizRepository.findById(id).map(quizMapper::quizToQuizDTO).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public QuizPatchDTO patchQuiz(Long id, QuizPatchDTO quizDTO) {

		return quizRepository.findById(id).map(quiz -> {

			if (quizDTO.getQuizTitle() != null)
				quiz.setQuizTitle(quizDTO.getQuizTitle());

			if (quizDTO.getQuizDescription() != null)
				quiz.setQuizDescription(quizDTO.getQuizDescription());

			if (quizDTO.getQuizCreateDateTime() != null)
				quiz.setQuizCreateDateTime(quizDTO.getQuizCreateDateTime());

	
			if (quizDTO.getIsRunning() != null)
				quiz.setIsRunning(quizDTO.getIsRunning());


			QuizPatchDTO returnDTO = quizMapper.quizToQuizPatchDTO(quizRepository.save(quiz));

			return returnDTO;

		}).orElseThrow(ResourceNotFoundException::new);

	}

	@Override
	public QuizPatchDTO updateQuiz(Long id, QuizPatchDTO quizDTO) {

		return quizRepository.findById(id).map(quiz -> {

			quiz.setIsRunning(quizDTO.getIsRunning());

			quiz.setQuizCreateDateTime(quizDTO.getQuizCreateDateTime());
			quiz.setQuizTitle(quizDTO.getQuizTitle());
			quiz.setQuizDescription(quizDTO.getQuizDescription());

			QuizPatchDTO returnDTO = quizMapper.quizToQuizPatchDTO(quizRepository.save(quiz));

			return returnDTO;
		}).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public void deleteQuiz(Long id) {
		
		Quiz quiz = quizRepository.findById(id).get();
		
		Set<Question> questions = quiz.getQuestions();
		
		Iterator<Question> quesIterator = questions.iterator();
		
		quiz.setQuestions(null);
		
		while(quesIterator.hasNext())
		{
			quesIterator.next().setQuiz(null);
		}

		quizRepository.deleteById(id);
		
	}

	private QuizPatchDTO saveAndReturnDTO(Quiz quiz) {
		Quiz savedQuiz = quizRepository.save(quiz);

		return quizMapper.quizToQuizPatchDTO(savedQuiz);
	}

	@Override
	public Set<QuizDTO> getAllQuiz() {

		return quizRepository.findAll().stream().map(quizMapper::quizToQuizDTO).collect(Collectors.toSet());
	}

	@Override
	public void addQuestionToQuiz(Long quizId, QuestionIdListDTO questionIdListDTO) {
		// TODO Auto-generated method stub
		
		List<Long> quesIds = questionIdListDTO.getQuizIds();
		Quiz quiz = quizRepository.findById(quizId).orElseThrow(ResourceNotFoundException::new);
		for(Long quesId : quesIds)
		{
			Question question = questionRepository.findById(quesId).orElseThrow(ResourceNotFoundException::new);
			quiz.addQuestion(question);
			
		}
		quizRepository.save(quiz);
	}

	@Override
	public void addStudentToQuiz(Long quizId, StudentIdDTO studentIdDTO) {
		
		Quiz quiz = quizRepository.findById(quizId).orElseThrow(ResourceNotFoundException::new);
		Long id = studentIdDTO.getStudId();
		Student student = studentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		
		
		StudentQuizScore key = new StudentQuizScore(studentIdDTO.getStudId(), quizId);
		StudentQuiz studentQuiz = new StudentQuiz(key, student, quiz, null);
		studentQuizRepository.save(studentQuiz);
		
		quiz.addStudent(studentQuiz);		
	}
}
