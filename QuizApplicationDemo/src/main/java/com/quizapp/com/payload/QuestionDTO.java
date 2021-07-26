package com.quizapp.com.payload;

import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class QuestionDTO {
	
	private Long id; 
	private String description;
	private Set<OptionDTO> options;
	
     public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<OptionDTO> getOptions() {
		return options;
	}
	public void setOptions(Set<OptionDTO> options) {
		this.options = options;
	}
	@Override
	public String toString() {
		return "QuestionDTO [id=" + id + ", description=" + description + ", options=" + options + "]";
	}


}
