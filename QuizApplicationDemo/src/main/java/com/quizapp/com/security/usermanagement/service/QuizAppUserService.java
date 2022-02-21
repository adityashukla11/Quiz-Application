package com.quizapp.com.security.usermanagement.service;

import com.quizapp.com.security.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface QuizAppUserService extends UserDetailsService {
    User getUserById(Long id);
}
