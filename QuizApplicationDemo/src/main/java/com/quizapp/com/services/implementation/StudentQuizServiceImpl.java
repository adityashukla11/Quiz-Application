package com.quizapp.com.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.mapper.StudentScoreMapper;
import com.quizapp.com.payload.StudentQuizDTO;
import com.quizapp.com.repository.QuizRepository;
import com.quizapp.com.repository.StudentQuizRepository;
import com.quizapp.com.repository.StudentRepository;
import com.quizapp.com.services.StudentQuizService;

@Service
public class StudentQuizServiceImpl implements StudentQuizService {

	private final StudentQuizRepository studentQuizRepository;
	private final StudentScoreMapper studentScoreMapper;
	private final QuizRepository quizRepository;
	private final StudentRepository studentRepository;

	public StudentQuizServiceImpl(StudentQuizRepository studentQuizRepository, StudentScoreMapper studentScoreMapper,
			QuizRepository quizRepository, StudentRepository studentRepository) {
		this.studentQuizRepository = studentQuizRepository;
		this.studentScoreMapper = studentScoreMapper;
		this.quizRepository = quizRepository;
		this.studentRepository = studentRepository;
	}

	@Override
	public List<StudentQuizDTO> getLeaderboardForQuiz(Long id) {

		List<StudentQuiz> tempList = studentQuizRepository.findAllByIdQuizIdOrderByScoreDesc(id);
		
		System.out.println(tempList);
		
		List<StudentQuizDTO> returnListOfDtos = new ArrayList<>();
		
		for(StudentQuiz stq : tempList)
		{
			StudentQuizDTO temp = studentScoreMapper.studentQuiztoStudentQuizDTO(stq);
			returnListOfDtos.add(temp);
		}
		
		return returnListOfDtos;

	}

	@Override
	public List<StudentQuizDTO> getfailedStudents(Long id, Integer passingMarks) {
		return studentQuizRepository.findAllByIdQuizId(id).stream()
				.filter(studentQuiz -> studentQuiz.getScore() < passingMarks)
				.map(studentScoreMapper::studentQuiztoStudentQuizDTO).collect(Collectors.toList());
	}

	@Override
	public StudentQuizDTO updateScoreOfaStudent(Long studId, Long quizId, Integer newScore) {

		StudentQuiz studentQuiz = findByQuizIdAndStudentIdHelper(quizId, studId);
		studentQuiz.setScore(newScore);
		return studentScoreMapper.studentQuiztoStudentQuizDTO(studentQuizRepository.save(studentQuiz));
	}

	@Override
	public StudentQuizDTO findByQuizIdAndStudentId(Long quizId, Long studId) {

		return studentScoreMapper.studentQuiztoStudentQuizDTO(findByQuizIdAndStudentIdHelper(quizId, studId));
	}

	private StudentQuiz findByQuizIdAndStudentIdHelper(Long quizId, Long studId) {

		Quiz quiz = quizRepository.findById(quizId).orElseThrow(ResourceNotFoundException::new);

		StudentQuiz studentQuiz = quiz.getStudentquiz().stream()
				.filter(temp -> temp.getQuiz().getId() == quizId && temp.getStudent().getId() == studId).findFirst()
				.orElseThrow(ResourceNotFoundException::new);

		return studentQuiz;
	}

}
