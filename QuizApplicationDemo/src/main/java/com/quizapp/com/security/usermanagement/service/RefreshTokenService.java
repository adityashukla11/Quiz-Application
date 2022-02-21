package com.quizapp.com.security.usermanagement.service;

public interface RefreshTokenService {
    String generateAndSaveRefreshTokenForUser(Long id,String oldToken);
    boolean isRefreshTokenValid(String token);
    void invalidateRefreshTokenForUser(Long id);

}
