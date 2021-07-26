package com.quizapp.com.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.payload.QuizDTO;
import com.quizapp.com.payload.QuizPatchDTO;
import com.quizapp.com.payload.StudentIdDTO;
import com.quizapp.com.payload.lists.QuestionIdListDTO;
import com.quizapp.com.payload.lists.QuestionListDTO;
import com.quizapp.com.payload.lists.QuizListDTO;
import com.quizapp.com.payload.lists.StudentListDTO;
import com.quizapp.com.services.QuizService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Quiz", description = "Endpoints to manage all Quiz")
@RestController
@RequestMapping("/api/topics/quizzes")
public class QuizController {

	private final QuizService quizService;

	public QuizController(QuizService quizService) {
		this.quizService = quizService;
	}

	@ApiOperation(value = "It gets all Quiz", notes = "It fetches all quiz active/inactive", response = QuizListDTO.class)
	@GetMapping
	public ResponseEntity<QuizListDTO> getAllQuizzes() {
		return new ResponseEntity<QuizListDTO>(new QuizListDTO(quizService.getAllQuiz()), HttpStatus.OK);
	}

	@ApiOperation(value = "It gets a Quiz by Id", notes = "It fetches a quiz by given Id", response = QuizDTO.class)
	@GetMapping({ "/{id}" })
	public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
		return new ResponseEntity<QuizDTO>(quizService.getQuiz(id), HttpStatus.OK);
	}

	@ApiOperation(value = "It gets all running Quiz", notes = "It gets all running quizzes", response = QuizListDTO.class)
	@GetMapping({ "/allRunningQuiz" })
	public ResponseEntity<QuizListDTO> getRunningQuiz() {
		return new ResponseEntity<QuizListDTO>(new QuizListDTO(quizService.getAllRunningQuiz()), HttpStatus.OK);

	}

	@ApiOperation(value = "It gets all previous quiz", notes = "It gets all previous quiz", response = QuizListDTO.class)
	@GetMapping({ "/allPreviousQuiz" })
	public ResponseEntity<QuizListDTO> getAllPreviousQuiz() {
		return new ResponseEntity<QuizListDTO>(new QuizListDTO(quizService.getAllpreviousQuiz()), HttpStatus.OK);
	}

	@ApiOperation(value = "It gets a Quiz before a date", notes = "It fetches all quiz before a certain date", response = QuizListDTO.class)
	@GetMapping({ "/allQuizBefore" })
	public ResponseEntity<QuizListDTO> getAllQuizBefore(
			@ApiParam(name = "date" ,value = "A date", example = "2021-06-19", required = true, defaultValue = "2021-01-01 00:00:00") @RequestParam(value = "date", required = false) String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime dateTime = LocalDateTime.parse(date + " 00:00:00.000", formatter);

		return new ResponseEntity<QuizListDTO>(new QuizListDTO(quizService.getAllQuizBeforeDate(dateTime)),
				HttpStatus.OK);
	}

	@ApiOperation(value = "It gets all registered student", notes = "It gives a list all student registered for a particular quiz", response = StudentListDTO.class)
	@GetMapping({ "/getAllRegisteredStudent/{id}" })
	public ResponseEntity<StudentListDTO> getAllRegisteredStudent(@PathVariable Long id) {
		return new ResponseEntity<StudentListDTO>(new StudentListDTO(quizService.getAllRegisteredStudents(id)),
				HttpStatus.OK);
	}

	@ApiOperation(value = "It gets all Questions in a Quiz", notes = "It fetches all Question that appeared in a given quiz", response = QuestionListDTO.class)
	@GetMapping({ "/getAllQuestions/{id}" })
	public ResponseEntity<QuestionListDTO> getAllQuestions(@PathVariable Long id) {
		return new ResponseEntity<QuestionListDTO>(new QuestionListDTO(quizService.getAllQuestion(id)), HttpStatus.OK);
	}

	@ApiOperation(value = "It creates a Quiz", notes = "It creates a new Quiz", response = QuizPatchDTO.class)
	@PostMapping
	public ResponseEntity<QuizPatchDTO> createNewQuiz(@RequestBody QuizPatchDTO quizDTO) {
		return new ResponseEntity<QuizPatchDTO>(quizService.createQuiz(quizDTO), HttpStatus.CREATED);
	}

	@ApiOperation(value = "It updates a Quiz", notes = "It updates a Quiz with given id", response = QuizPatchDTO.class)
	@PutMapping({ "/{id}" })
	public ResponseEntity<QuizPatchDTO> updateQuiz(@PathVariable Long id, @RequestBody QuizPatchDTO quizDTO) {
		return new ResponseEntity<QuizPatchDTO>(quizService.updateQuiz(id, quizDTO), HttpStatus.OK);
	}

	@ApiOperation(value = "It patches a Quiz", notes = "It updates only selective field of the quiz", response = QuizPatchDTO.class)
	@PatchMapping({ "/{id}" })
	public ResponseEntity<QuizPatchDTO> patchQuiz(@PathVariable Long id,
			@ApiParam("Only provide those field you need to update") @RequestBody QuizPatchDTO quizDTO) {
		return new ResponseEntity<QuizPatchDTO>(quizService.patchQuiz(id, quizDTO), HttpStatus.OK);
	}

	@ApiOperation(value = "It deletes a Quiz", notes = "It deletes a quiz with given id")
	@DeleteMapping({ "/{id}" })
	public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
		try {
			quizService.deleteQuiz(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		catch (ResourceNotFoundException e) {
				throw new ResourceNotFoundException();
		}
		
	}

	@ApiOperation(value = "It add questions to quiz", notes = "It adds questions to an existing quiz")
	@PostMapping({ "/addQuestionsToQuiz" })
	public ResponseEntity<Void> addQuestionToTopics(@RequestParam(value = "quizId", required = true) String quizId,
			@RequestBody QuestionIdListDTO questionIdListDTO) {
		Long id = Long.parseLong(quizId);

		quizService.addQuestionToQuiz(id, questionIdListDTO);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "It should register a student to a  quiz", notes = "It adds student to a quiz")
	@PostMapping({ "/addStudentToQuiz" })
	public ResponseEntity<Void> addStudentToQuiz(@RequestParam(value = "quizId", required = true) Long quizId,
			@RequestBody StudentIdDTO studentIdDTO) {
		quizService.addStudentToQuiz(quizId, studentIdDTO);

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
