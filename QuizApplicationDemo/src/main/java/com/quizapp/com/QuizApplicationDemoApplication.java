package com.quizapp.com;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import com.fasterxml.classmate.ResolvedType;
import com.quizapp.com.domain.Admin;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@ConfigurationPropertiesScan
@EntityScan({"com.quizapp.com.domain","com.quizapp.com.security"})
@EnableSwagger2
public class QuizApplicationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizApplicationDemoApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.quizapp"))
				.paths(PathSelectors.any()).build().apiInfo(apiDetails());

	}

	private ApiInfo apiDetails() {
		return new ApiInfo("Quiz Application API", "REST API's for a Quiz Application", "1.0.0", "FREE",
				new Contact("Aditya Shukla", "", "adityashukla@fico.com"), "API License", "", Collections.emptyList());
//		
	}
}
