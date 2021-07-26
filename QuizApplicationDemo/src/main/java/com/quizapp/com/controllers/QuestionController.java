package com.quizapp.com.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.payload.QuestionDTO;
import com.quizapp.com.payload.lists.QuestionListDTO;
import com.quizapp.com.services.QuestionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Question", description = "Endpoints to manage all Questions that appear in Quiz")
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

	private QuestionService questionService;

	public QuestionController(QuestionService questionService) {
		this.questionService = questionService;
	}

	@ApiOperation(value = "It gets a Question by Id", notes = "It gets a question with given question-id", response = QuestionDTO.class)
	@GetMapping({"/{id}"})
	public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
		return new ResponseEntity<QuestionDTO>(questionService.getQuestion(id), HttpStatus.OK);
	}

	@ApiOperation(value = "It gets all Question", notes = "It gets all questions", response = QuestionListDTO.class)
	@GetMapping
	public ResponseEntity<QuestionListDTO> getAllQuestion() {
		return new ResponseEntity<QuestionListDTO>(new QuestionListDTO(questionService.getAllQuestion()),
				HttpStatus.OK);
	}

	@ApiOperation(value = "It searches a Question by keyword", notes = "It searches for a question with given keyword", response = QuestionListDTO.class)
	@GetMapping({"/searchByKeyword/{keyword}"})
	public ResponseEntity<QuestionListDTO> getAllQuestionByKeyword(@PathVariable String keyword) {
		return new ResponseEntity<QuestionListDTO>(
				new QuestionListDTO(questionService.getQuestionByKeywords(keyword)),
				HttpStatus.OK);
	}
	
	@ApiOperation(value = "It creates a Question", notes = "It creates a new Question", response = QuestionDTO.class)
	@PostMapping
	public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
		return new ResponseEntity<QuestionDTO>(questionService.createQuestion(questionDTO),HttpStatus.CREATED);
	}

	@ApiOperation(value = "It patches a Question", notes = "It only updates selected fields of a Question with given id", response = QuestionDTO.class)
	@PatchMapping({"/{id}"})
	public ResponseEntity<QuestionDTO> patchQuestion(@ApiParam(name = "id",required = true,example = "1",value = "Question Id") @PathVariable Long id , @ApiParam(name = "Only provide fields you need to update") @RequestBody QuestionDTO questionDTO) {
		return new ResponseEntity<QuestionDTO>(questionService.patchQuestion(id, questionDTO),HttpStatus.OK);
	}

	@ApiOperation(value = "It updates a Question", notes = "It updates a question with given question-id", response = QuestionDTO.class)
	@PutMapping({"/{id}"})
	public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
		return new ResponseEntity<QuestionDTO>(questionService.updateQuestion(id, questionDTO),HttpStatus.OK);
	}

	@ApiOperation(value = "It deletes a question", notes = "It deletes a question with given question-id")
	@DeleteMapping({"/{id}"})
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
		try {
			questionService.deleteQuestion(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ResourceNotFoundException();
		}
	
	}
}
