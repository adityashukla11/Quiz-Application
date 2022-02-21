package com.quizapp.com.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizapp.com.security.dto.LoginRequestDTO;
import com.quizapp.com.security.jwt.JwtAuthenticationUtil;
import com.quizapp.com.security.jwt.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class CustomUserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;
  private JwtProperties jwtProperties;
  private JwtAuthenticationUtil jwtAuthenticationUtil;
  private UrlPathHelper urlPathHelper;

  public CustomUserAuthenticationFilter(AuthenticationManager authenticationManager,
                                        JwtAuthenticationUtil jwtAuthenticationUtil, JwtProperties jwtProperties, UrlPathHelper urlPathHelper) {
    this.authenticationManager = authenticationManager;
    this.jwtAuthenticationUtil = jwtAuthenticationUtil;
    this.jwtProperties = jwtProperties;
    this.urlPathHelper = urlPathHelper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    LoginRequestDTO loginRequestDTO = null;
    try {
      loginRequestDTO = new ObjectMapper().readValue(request.getInputStream(),
              LoginRequestDTO.class);
    } catch (IOException e) {
      log.error("Error while converting converting request to loginDto {} ", e);
      throw new RuntimeException(e.getMessage());
    }
    log.info("Email="+loginRequestDTO.getEmail());
      Authentication authentication = new UsernamePasswordAuthenticationToken(
              loginRequestDTO.getEmail(),
             loginRequestDTO.getPassword()
      );
      log.info("Performing authentication in CustomAuthenticationFilter");
      //Perform Authentication using email and password.
      Authentication authenticate = authenticationManager.authenticate(authentication);
      return authenticate;
  }

  @Override
  public void setPostOnly(boolean postOnly) {
    super.setPostOnly(true);
  }

  @Override
  public void setUsernameParameter(String usernameParameter) {
    super.setUsernameParameter("email");
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    log.info("Authorization successful");
    String jwtToken = jwtAuthenticationUtil.generateJwtAuthToken(authResult);
    response.addHeader(HttpHeaders.AUTHORIZATION,jwtProperties.getJwtPrefix() + " " + jwtToken);
    chain.doFilter(request,response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    log.info("Authentication for user failed " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request));
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.getOutputStream().println("{ \"error\": \"" + "You are unauthorized. Please contact administration" +
            "\" }");

  }

  @Override
  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }


}

