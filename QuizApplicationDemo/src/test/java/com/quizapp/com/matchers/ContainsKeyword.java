package com.quizapp.com.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ContainsKeyword extends TypeSafeMatcher<String>{

	private String keyword;
	
	

	public ContainsKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public void describeTo(Description description) {
	description.appendText("it contains keyword" +keyword);
		
	}

	@Override
	protected boolean matchesSafely(String item) {
		return item.contains(keyword);
	}

}
