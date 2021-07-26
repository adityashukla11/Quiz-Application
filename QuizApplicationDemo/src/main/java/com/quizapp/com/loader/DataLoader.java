package com.quizapp.com.loader;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.quizapp.com.domain.Option;
import com.quizapp.com.domain.Question;
import com.quizapp.com.domain.Quiz;
import com.quizapp.com.domain.Student;
import com.quizapp.com.domain.StudentQuiz;
import com.quizapp.com.domain.StudentQuizScore;
import com.quizapp.com.domain.Topic;
import com.quizapp.com.repository.QuestionRepository;
import com.quizapp.com.repository.QuizRepository;
import com.quizapp.com.repository.StudentQuizRepository;
import com.quizapp.com.repository.StudentRepository;
import com.quizapp.com.repository.TopicRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile({"dev","prod"})
public class DataLoader implements ApplicationListener<ContextRefreshedEvent>{

	private final QuizRepository quizRepository;
	private final StudentRepository studentRepository;
	private final TopicRepository topicRepository;
	private final QuestionRepository questionRepository;
	private final StudentQuizRepository studentQuizRepository;
	
	
	public DataLoader(QuizRepository quizRepository, StudentRepository studentRepository,
			TopicRepository topicRepository, QuestionRepository questionRepository,
			StudentQuizRepository studentQuizRepository) {
		this.quizRepository = quizRepository;
		this.studentRepository = studentRepository;
		this.topicRepository = topicRepository;
		this.questionRepository = questionRepository;
		this.studentQuizRepository = studentQuizRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(topicRepository.count() == 0) 
		loadData();
		
	}
	
	 void loadData()
	 {
		 Topic cricketTopic = new Topic();
		 cricketTopic.setTopicCode("CK21");
		 cricketTopic.setTopicDescription("A Cricket Topic");
		 cricketTopic.setTopicName("Cricket");
		 topicRepository.save(cricketTopic);
		 
		 Topic gkTopic = new Topic();
		 gkTopic.setTopicCode("GK21");
		 gkTopic.setTopicDescription("General Knowledge Topic");
		 gkTopic.setTopicName("General Knowledge");
		 topicRepository.save(gkTopic);
		 
		 
		 Quiz quizCricket = new Quiz();
		 quizCricket.setIsRunning(true);
		 quizCricket.setQuizCreateDateTime(LocalDateTime.of(2021,6,23,0,0));
		 quizCricket.setQuizTitle("Quiz on Cricket");
		 quizCricket.setQuizDescription("Quiz on Cricket. Answer all question correctly");
		 quizRepository.save(quizCricket);
		 
		 cricketTopic.getQuizzes().add(quizCricket);
		 quizCricket.getTopics().add(cricketTopic);
		 
		 
		 Student adityaStudent = new Student();
		 adityaStudent.setName("Aditya Shukla");
		 adityaStudent.setEducation("BTECH");
		 
		 Student mehulStudent = new Student();
		 mehulStudent.setName("Mehul Kartikeyan");
		 mehulStudent.setEducation("MBBS");
	
		 studentRepository.save(adityaStudent);
		 studentRepository.save(mehulStudent);
		 
		
		 
	
		 
		 Question questionCricket1 = new Question();
		 questionCricket1.setDescription("Who won the first IPL ?");
		 
		 Question questionCricket2 = new Question();
		 questionCricket2.setDescription("How many international century does Virat Kohli has ?");
		 
		 Option crik1 = new Option();
		 Option crik2 = new Option();
		 crik1.setText("RR");
		 crik2.setText("CSK");
		 
		 
		 Option crik3 = new Option();
		 Option crik4 = new Option();
		 crik3.setText("65");
		 crik4.setText("70");
		 
		 questionRepository.save(questionCricket1);
		 questionRepository.save(questionCricket2);
		 
		
		 
		 
		 questionCricket1.getOptions().add(crik1);
		 questionCricket1.getOptions().add(crik2);
		 questionCricket1.setCorrectOption(crik1);
		
		 
		 questionCricket2.getOptions().add(crik3);
		 questionCricket2.getOptions().add(crik4);
		 questionCricket2.setCorrectOption(crik4);

		 
		 quizCricket.getQuestions().add(questionCricket2);
		 quizCricket.getQuestions().add(questionCricket1);
		 
		 questionCricket1.setQuiz(quizCricket);
		 questionCricket2.setQuiz(quizCricket);
		 
		
		 
		 
		 
		 StudentQuiz mehulQuiz = new StudentQuiz();
		 mehulQuiz.setId(new StudentQuizScore(mehulStudent.getId(),quizCricket.getId()));
		 mehulQuiz.setStudent(mehulStudent);
		 mehulQuiz.setQuiz(quizCricket);
		 mehulQuiz.setScore(10);
		 studentQuizRepository.save(mehulQuiz);
		 
		 StudentQuiz adityaQuiz = new StudentQuiz();
		 adityaQuiz.setId(new StudentQuizScore(adityaStudent.getId(),quizCricket.getId()));
		 adityaQuiz.setQuiz(quizCricket);
		 adityaQuiz.setStudent(adityaStudent);
		 adityaQuiz.setScore(5);
			
		 studentQuizRepository.save(adityaQuiz);
		 
		 mehulStudent.getStudentquiz().add(mehulQuiz);
		 adityaStudent.getStudentquiz().add(adityaQuiz);
		 
	
		 quizCricket.getStudentquiz().add(mehulQuiz);
		 quizCricket.getStudentquiz().add(adityaQuiz);
		 
	 }
	

} 
