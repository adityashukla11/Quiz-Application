package com.quizapp.com.security.usermanagement.service.implementation;

import com.quizapp.com.security.jwt.config.RefreshTokenProperties;
import com.quizapp.com.security.model.RefreshToken;
import com.quizapp.com.security.model.User;
import com.quizapp.com.security.usermanagement.repository.RefreshTokenRepository;
import com.quizapp.com.security.usermanagement.service.QuizAppUserService;
import com.quizapp.com.security.usermanagement.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private RefreshTokenRepository refreshTokenRepository;
  private RefreshTokenProperties refreshTokenConfiguration;
  private QuizAppUserService quizAppUserService;

  public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, RefreshTokenProperties refreshTokenConfiguration,
                               QuizAppUserService quizAppUserService) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.refreshTokenConfiguration = refreshTokenConfiguration;
    this.quizAppUserService = quizAppUserService;
  }

  @Override
  public String generateAndSaveRefreshTokenForUser(Long id,String oldToken) {
    if(!isRefreshTokenValid(oldToken)){
      log.error("Refresh Token is invalid {} ",oldToken);
      throw new RuntimeException("Refresh Token is Invalid");
    }
    RefreshToken refreshToken = RefreshToken.builder().user(quizAppUserService.getUserById(id))
            .authToken(UUID.randomUUID().toString()).iat(Instant.now())
            .eat(Instant.now().plusSeconds(refreshTokenConfiguration.getTokenExpiryAfterSeconds()))
            .build();
    refreshTokenRepository.save(refreshToken);
    return refreshToken.getAuthToken();
  }

  @Override
  public boolean isRefreshTokenValid(String token) {
   Optional<RefreshToken> tokenToVerifyOptional = refreshTokenRepository.findByAuthToken(token);
   if(!tokenToVerifyOptional.isPresent()){
     return false;
   }
   RefreshToken tokenToVerify = tokenToVerifyOptional.get();
   if(tokenToVerify == null){
     log.error("Refresh Token is null ");
     return false;
   }
   if(tokenToVerify.getEat().isAfter(Instant.now())){
     log.error("Refresh Token has expired. Redirect Back to Sign in");
     return false;
   }
   return true;
  }

  @Override
  public void invalidateRefreshTokenForUser(Long id) {
    User user = quizAppUserService.getUserById(id);
    refreshTokenRepository.deleteByUser(user);
  }

}
