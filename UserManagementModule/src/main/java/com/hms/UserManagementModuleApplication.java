package com.hms;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.class})
@EnableJpaAuditing
public class UserManagementModuleApplication {

    @Bean
    ModelMapper getModelMapper() {
    	ModelMapper mapper = new ModelMapper();
    	//mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;	
	}

	public static void main(String[] args) {		
		SpringApplication.run(UserManagementModuleApplication.class, args);
	}

}
