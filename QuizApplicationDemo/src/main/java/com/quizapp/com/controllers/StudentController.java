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
import com.quizapp.com.payload.StudentDTO;
import com.quizapp.com.payload.lists.StudentListDTO;
import com.quizapp.com.services.StudentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Student",description = "Endpoints to manage all students")
@RestController
@RequestMapping("/api/students")
public class StudentController {

	private StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@ApiOperation(value = "It gets a student by id",notes = "Returns a student with given student-id", response = StudentDTO.class)
	@GetMapping({ "/{id}" })
	public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
		return new ResponseEntity<StudentDTO>(studentService.getStudentById(id), HttpStatus.OK);
	}

	@ApiOperation(value = "It gets all student",notes = "It gets a list of all student", response = StudentListDTO.class)
	@GetMapping
	public ResponseEntity<StudentListDTO> getAllStudents() {
		return new ResponseEntity<StudentListDTO>(new StudentListDTO(studentService.getAllStudents()), HttpStatus.OK);
	}

	@ApiOperation(value = "Creates a new Student",notes = "It creates a new Student", response = StudentDTO.class)
	@PostMapping
	public ResponseEntity<StudentDTO> createNewStudent(@RequestBody StudentDTO studentDTO) {

		return new ResponseEntity<StudentDTO>(studentService.createNewStudent(studentDTO), HttpStatus.CREATED);
	}

	@ApiOperation(value = "It updates a student",notes = "It updates a student with given student-id", response = StudentDTO.class)
	@PutMapping({ "/{id}" })
	public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
		return new ResponseEntity<StudentDTO>(studentService.updateStudent(id, studentDTO), HttpStatus.OK);
	}

	@ApiOperation(value = "It patches a student by id",notes = "It only updates selected field of a student with given student-id", response = StudentDTO.class)
	@PatchMapping({ "/{id}" })
	public ResponseEntity<StudentDTO> patchStudent(@PathVariable Long id, @ApiParam("Only provide the fields you need to update ")@RequestBody StudentDTO studentDTO) {
		return new ResponseEntity<StudentDTO>(studentService.patchStudent(id, studentDTO), HttpStatus.OK);
	}

	@ApiOperation(value = "It deletes a student by id",notes = "It deletes a student with given student-id")
	@DeleteMapping({ "/{id}" })
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		try {
			studentService.deleteStudentById(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
		 throw new ResourceNotFoundException();
		}
	
	}
}
