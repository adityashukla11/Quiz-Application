package com.quizapp.com.security.usermanagement.service.implementation;

import com.quizapp.com.security.model.QuizAppUser;
import com.quizapp.com.security.model.User;
import com.quizapp.com.security.usermanagement.repository.UserRepository;
import com.quizapp.com.security.usermanagement.service.QuizAppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class QuizAppUserServiceImpl implements QuizAppUserService {

    private UserRepository userRepository;

    public QuizAppUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Taking email as the username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) {
            log.error("The user with given email doesn't exist {}", email);
            throw new UsernameNotFoundException("User with given email does not exist");
        }
        return getUserDetailObjectFromUser(user.get());
    }
    public User getUserById(Long id){
        return userRepository.findById(id).map((user) -> {
            getUserDetailObjectFromUser(user);
            return user;
        }).orElseThrow(() -> new RuntimeException("User with given id not found"));
    }
    private QuizAppUser getUserDetailObjectFromUser(User user) {
        Set<GrantedAuthority> grantedAuthoritySet = user.getRoles().stream().map((role) ->
                        new SimpleGrantedAuthority("ROLE_"+role.getRole().name()))
                 .collect(Collectors.toSet());
        QuizAppUser quizAppUser = QuizAppUser.builder().id(user.getId()).email(user.getEmail())
                .password(user.getPassword()).authorities(grantedAuthoritySet)
                .isAccountNonExpired(true).isCredentialNonExpired(true).isEnabled(true).isAccountNonLocked(true)
                .build();
        //Throw exception if user is locked or not available.
        try {
            AccountStatusUserDetailsChecker checkStatus = new AccountStatusUserDetailsChecker();
            checkStatus.check(quizAppUser);
        }catch (Exception exception){
            log.error("User account is disabled or expired {} ",exception.getMessage());
            throw new RuntimeException("User account is locked or expired. Please try again later");
        }
        return quizAppUser;
    }
}
