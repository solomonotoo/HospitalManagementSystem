package com.hms.ps.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

	
		@Value("${frontend.url}")
  private String frontendUrl; //front end url

  @Bean
  WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
          @Override
          public void addCorsMappings(CorsRegistry registry) {
              registry.addMapping("/**") //add the cors mapping
                      .allowedOrigins(frontendUrl) //allow origins or any request originated from the frontend url
                      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")//http method to be allowed
                      .allowedHeaders("*")//allow all headers
                      .allowCredentials(true) //allows credentials, cookies and sending of data from the fronend url 
                      .maxAge(3600);//set cors cache for 1 hr
          }
      };
  }
	
}
