package com.quizapp.com.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizapp.com.payload.StudentQuizDTO;
import com.quizapp.com.payload.lists.StudentQuizListDTO;
import com.quizapp.com.services.StudentQuizService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Leaderboard",description = "Endpoints to retrieve leadboards for a Quiz")
@RestController
@RequestMapping("/api/topics/quizzes/result")
public class StudentQuizController {

	private StudentQuizService studentQuizService;

	public StudentQuizController(StudentQuizService studentQuizService) {
		this.studentQuizService = studentQuizService;
	}

	@ApiOperation(value = "Get all student below a certain mark",notes = "Gets all student below a given mark in a given quiz")
	@GetMapping({ "/getAllFailedStudent/{id}/{score}" })
	public ResponseEntity<StudentQuizListDTO> getFailedStudent(
			@ApiParam(name = "id",example = "1",required = true,value = "The id of Quiz") 
			@PathVariable Long id, @PathVariable Integer score) {
		return new ResponseEntity<StudentQuizListDTO>(
				new StudentQuizListDTO(studentQuizService.getfailedStudents(id, score)), HttpStatus.OK);
	}

	@ApiOperation(value = "Get Current Leaderboard",notes = "Gets the current leaderboard for a particular Quiz")
	@GetMapping({ "/leaderboard/{id}" })
	public ResponseEntity<StudentQuizListDTO> getLeaderboard(@PathVariable Long id) {
		return new ResponseEntity<StudentQuizListDTO>(
				new StudentQuizListDTO(studentQuizService.getLeaderboardForQuiz(id)), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get current info of student in a quiz",notes = "Get the score and other details of a student who participated in a Quiz")
    @GetMapping({"/getStudenetQuizInfo"})
	public ResponseEntity<StudentQuizDTO> getInfoOfAStudentInQuiz(@RequestParam Long studId, @RequestParam Long quizId)
	{
		return new ResponseEntity<StudentQuizDTO>(studentQuizService.findByQuizIdAndStudentId(quizId, studId),HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update the score of a Student ",notes = "Updates the score of a student who participated in a Quiz")
    @PostMapping({"/updateStudentQuizScore"})
	public ResponseEntity<StudentQuizDTO> updateStudentQuizScore(@RequestParam Long studId, @RequestParam Long quizId, @RequestParam Integer score )
	{
		return new ResponseEntity<StudentQuizDTO>(studentQuizService.updateScoreOfaStudent(studId, quizId, score),HttpStatus.OK);
	}
	
}
