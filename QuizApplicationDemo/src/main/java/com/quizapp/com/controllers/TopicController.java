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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizapp.com.controllers.exceptions.ResourceNotFoundException;
import com.quizapp.com.domain.Admin;
import com.quizapp.com.mapper.TopicMapper;
import com.quizapp.com.payload.TopicDTO;
import com.quizapp.com.payload.TopicPatchDTO;
import com.quizapp.com.payload.lists.TopicIdListDTO;
import com.quizapp.com.payload.lists.TopicListDTO;
import com.quizapp.com.services.TopicService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags =  "Topics",description = "Endpoints to manage Topics for the Quiz")
@RestController
@RequestMapping("/api/topics")
public class TopicController {

	private TopicService topicService;
	

	public TopicController(TopicService topicService) {
		this.topicService = topicService;
	}

	@ApiOperation(value = "Get All Topics", notes = "It provides all the Quiz Topics", response = TopicListDTO.class)
	@GetMapping
	public ResponseEntity<TopicListDTO> getAllTopics()
	{
		return new ResponseEntity<TopicListDTO>(new TopicListDTO(topicService.getAllTopics()),HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get a Topic by Id", notes = "It provides the topic when the id is provided", response = TopicDTO.class)
	@GetMapping({"/{id}"})
	public ResponseEntity<TopicDTO> getTopicById(@PathVariable Long id)
	{
		return new ResponseEntity<TopicDTO>(topicService.getTopicById(id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Create a Topic", notes = "Creates a new Topic", response = TopicPatchDTO.class)
	@PostMapping
	public ResponseEntity<TopicPatchDTO> createNewTopic(@RequestBody TopicPatchDTO topicPatchDTO)
	{
		return new ResponseEntity<TopicPatchDTO>(topicService.createNewTopic(topicPatchDTO) , HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Updtes a Topic", notes = "It updates a given Topic with Topic-Id", response = TopicPatchDTO.class)
	@PutMapping({"/{id}"})
	public ResponseEntity<TopicPatchDTO> updateTopic(
	@ApiParam(name = "id",example = "1", required = true,value = "Topic Id",defaultValue = "1") 
	@PathVariable Long id, @RequestBody TopicPatchDTO topicPatchDTO)
	{
		return new ResponseEntity<TopicPatchDTO>(topicService.updateTopic(id, topicPatchDTO),HttpStatus.OK);
	}
	
	@ApiOperation(value = "It patches a topic", notes = "It updates the particular fields of Topic", response = TopicPatchDTO.class)
	@PatchMapping({"/{id}"})
	public ResponseEntity<TopicPatchDTO> patchTopic(@ApiParam(name = "id",example = "1",required = true,value = "Topic Id",defaultValue = "1")  @PathVariable Long id, @ApiParam(name = "Only provide the relevant field you want to update") @RequestBody TopicPatchDTO topicPatchDTO)
	{
		return new ResponseEntity<TopicPatchDTO>(topicService.patchTopic(id, topicPatchDTO),HttpStatus.OK);
	}
	
	@ApiOperation(value = "It deletes a Topic ", notes = "It deletes the Topic with given Topic id")
	@DeleteMapping({"/{id}"})
	public ResponseEntity<Void> deleteTopic(@PathVariable Long id)
	{
		try {
			topicService.deleteTopicById(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ResourceNotFoundException();
		}
		
	}
	
	@ApiOperation(value = "It searches all Topic by Topic-Name", notes = "It searches for all topics with the given Topic Name", response = TopicListDTO.class)
	@GetMapping({"searchByName/{keyword}"})
	public ResponseEntity<TopicListDTO> searchTopicByName(@ApiParam(name = "keyword") @PathVariable String keyword)
	{
		return new ResponseEntity<TopicListDTO>(new TopicListDTO(topicService.searchTopicByName(keyword)),HttpStatus.OK);
	}
	
	@ApiOperation(value = "It add topics to quiz", notes = "It adds topics to an existing quiz")
	@PostMapping({"/addQuizToTopics"})
	public ResponseEntity<Void> addQuizToTopics(@RequestParam (value = "quizId",required = true) Long quizId , @RequestBody TopicIdListDTO topicIdListDTO)
	{
//		Long id = Long.parseLong(quizId);
		
		topicService.addQuizToTopics(quizId, topicIdListDTO);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
