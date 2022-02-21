package com.quizapp.com.security.jwt;

import com.quizapp.com.security.jwt.config.JwtProperties;
import com.quizapp.com.security.model.QuizAppUser;
import com.quizapp.com.security.usermanagement.service.QuizAppUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@AllArgsConstructor
@Service
public class JwtAuthenticationUtil {

    private SecretKey secretKey;
    private final JwtProperties jwtConfig;
    private QuizAppUserService quizAppUserService;

    public String generateJwtAuthToken(Authentication authentication) {
        QuizAppUser quizAppUser = (QuizAppUser) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((quizAppUser.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(
                        LocalDateTime.now().plusMinutes(jwtConfig.getJwtExpirationAfterMinutes()).toLocalDate()))
                .signWith(secretKey)
                .compact();

    }
    public Authentication createAuthenticationObjectFromJwt(String token){
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        String email = claimsJws.getBody().getSubject();
        QuizAppUser quizAppUser = (QuizAppUser) quizAppUserService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
               quizAppUser, null, quizAppUser.getAuthorities());
        return authentication;
    }
    public boolean isJwtAuthTokenValid(String token)  {
        try{
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        }catch (JwtException exception){
            throw new RuntimeException("The access token is invalid : " + token);
        }catch (Exception e ){
            throw new RuntimeException("Something went wrong!");
        }
        return true;
    }
}
