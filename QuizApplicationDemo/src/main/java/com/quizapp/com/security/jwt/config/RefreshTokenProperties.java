package com.quizapp.com.security.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "quizapp.refresh-token")
@Getter
@Setter
public class RefreshTokenProperties {
    private Integer tokenExpiryAfterSeconds;
}
