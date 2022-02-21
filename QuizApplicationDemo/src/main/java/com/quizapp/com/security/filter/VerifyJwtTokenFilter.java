package com.quizapp.com.security.filter;

import com.quizapp.com.security.jwt.JwtAuthenticationUtil;
import com.quizapp.com.security.jwt.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class VerifyJwtTokenFilter extends OncePerRequestFilter {
  private JwtAuthenticationUtil jwtAuthenticationUtil;
  private JwtProperties jwtProperties;

  public VerifyJwtTokenFilter(JwtAuthenticationUtil jwtAuthenticationUtil, JwtProperties jwtProperties) {
    this.jwtAuthenticationUtil = jwtAuthenticationUtil;
    this.jwtProperties = jwtProperties;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    //If there is no header, just pass-on the request to the next filter.
    if( header == null || header.length() == 0 || header == ""|| !header.startsWith(jwtProperties.getJwtPrefix())){
      logger.info("Unauthenticated Request");
      filterChain.doFilter(request,response);
      return;
    }
    String authToken = "";
    try {
      authToken = header.substring(jwtProperties.getJwtPrefix().length());
      if(!jwtAuthenticationUtil.isJwtAuthTokenValid(authToken)){
        logger.error("ERROR! Invalid Token");
        throw new Exception("ERROR! Invalid Token");
      }
    } catch (Exception e ) {
      throw new RuntimeException("Invalid Access for token " + authToken);
    }
    //Token is valid.
    log.info("Creating authentication for user with token {} ", authToken);
    Authentication authentication = jwtAuthenticationUtil.createAuthenticationObjectFromJwt(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request,response);
  }

}
