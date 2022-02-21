package com.quizapp.com.security.config;

import com.quizapp.com.security.filter.CustomUserAuthenticationFilter;
import com.quizapp.com.security.filter.VerifyJwtTokenFilter;
import com.quizapp.com.security.jwt.JwtAuthenticationUtil;
import com.quizapp.com.security.jwt.config.JwtProperties;
import com.quizapp.com.security.usermanagement.service.QuizAppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UrlPathHelper;

@EnableWebSecurity
@Configuration
@Slf4j
public class QuizAppSecurityConfig extends WebSecurityConfigurerAdapter {

  private QuizAppUserService quizAppUserService;
  private JwtProperties jwtProperties;
  private JwtAuthenticationUtil jwtAuthenticationUtil;
  private UrlPathHelper urlPathHelper;

  public QuizAppSecurityConfig(QuizAppUserService quizAppUserService, JwtProperties jwtProperties, JwtAuthenticationUtil jwtAuthenticationUtil, UrlPathHelper urlPathHelper) {
    this.quizAppUserService = quizAppUserService;
    this.jwtProperties = jwtProperties;
    this.jwtAuthenticationUtil = jwtAuthenticationUtil;
    this.urlPathHelper = urlPathHelper;
  }


  //Register beans

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProviderBean = new DaoAuthenticationProvider();
    daoAuthenticationProviderBean.setUserDetailsService(quizAppUserService);
    daoAuthenticationProviderBean.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProviderBean;
  }

  @Bean
  public CustomUserAuthenticationFilter customUserAuthenticationFilter() throws Exception {
    return new CustomUserAuthenticationFilter(authenticationManager(),
            jwtAuthenticationUtil,jwtProperties, urlPathHelper);
  }

  @Bean
  public VerifyJwtTokenFilter verifyJwtTokenFilter() throws Exception {
    return new VerifyJwtTokenFilter(jwtAuthenticationUtil,jwtProperties);
  }

  /*---Error Fix ----*/
  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  //configurations

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable().sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
              .addFilter(customUserAuthenticationFilter())
              .addFilterAfter(verifyJwtTokenFilter(),CustomUserAuthenticationFilter.class)
              .authorizeRequests()
              .antMatchers(HttpMethod.POST,"/login").permitAll()
              .anyRequest().authenticated();

  }

  //Allow swagger ui without authentication
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**","/h2-console/**",
            "/login/**", "/signup/**",
            "/","index.html","/css/**","/scss/**","/js/**");
  }
}
