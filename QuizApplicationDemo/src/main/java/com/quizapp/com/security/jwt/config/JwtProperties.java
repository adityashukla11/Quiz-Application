package com.quizapp.com.security.jwt.config;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

@ConfigurationProperties(prefix = "quizapp.jwt")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JwtProperties {

    private String secretKey;
    private String jwtPrefix;
    private Integer jwtExpirationAfterMinutes;

    @Bean
    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
