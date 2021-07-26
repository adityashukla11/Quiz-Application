package com.quizapp.com.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(description = "This is Admin Model")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "admin_name", nullable = false)
	private String name;
	
	@Column(name = "admin_age" , nullable = true)
	private Integer age;
	
}

