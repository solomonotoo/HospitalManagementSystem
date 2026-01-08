package com.hms.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hms.jwt.AuthEntryPointJwt;
import com.hms.jwt.AuthTokenFilter;
import com.hms.oauth2config.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${frontend.url}")
    private String frontendUrl; //front end url
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
//	public SecurityConfig(AuthEntryPointJwt unauthorizedHandler) {
//		super();
//		this.unauthorizedHandler = unauthorizedHandler;
//	}
	
	@Autowired
	@Lazy
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		//disable csrf(cross site request forgery) token
		//http.csrf(csrf -> csrf.disable());
		
		//enables or configure csrf token
		//NB Once csrf token is enabled you need to provide a way for the frontend to be able to use it. One
		//way is creating csrf token endpoint controller
		 
		http.cors(Customizer.withDefaults()) // Ensure CORS is enabled
		.csrf(csrf -> 
		//An API to allow changing the method in which the expected CsrfToken is associated to the HttpServletRequest. 
		//For example, it may be stored in HttpSession.
				csrf.csrfTokenRepository(
						// a repository that stores csrf token in a cookie format.
						//A CsrfTokenRepository that persists the CSRF token in a cookie named"XSRF-TOKEN" and reads from the header 
						//"X-XSRF-TOKEN" following the conventions ofAngularJS.
						CookieCsrfTokenRepository
						//create an instance that creates cookies where Cookie.isHttpOnly() is set to false.
						.withHttpOnlyFalse()
						)
				
				//below is optional 
				//Allows specifying HttpServletRequest that should not use CSRF Protectioneven if they match the requireCsrfProtectionMatcher(RequestMatcher). 
    			//for pages that you want csrf protection to be ignore when users try to access those pages.
    			//e.g contact page, about page etc. these pages are usually open to public and anyone
    			//can access these pages
				.ignoringRequestMatchers("/api/auth/public/**","/api/v1/patients","/api/v1/contactus")
				);
		
		http.authorizeHttpRequests((requests) -> { requests
			.requestMatchers("/api/v1/check_email","/api/csrf-token").permitAll()
         .requestMatchers("/api/v1/users").permitAll() // Allow access to this endpoint
         .requestMatchers("/api/v1/patients","/api/v1/contactus").permitAll()
         .requestMatchers("/api/auth/public/**").permitAll()
         .requestMatchers("/oauth2/**").permitAll()
         .anyRequest().authenticated();
		})
		.oauth2Login(oauth2 -> 
				oauth2.successHandler(oAuth2LoginSuccessHandler));
		
		
		//unauthorizedHandler set as default exception mechanism for authentication
        http.exceptionHandling(exception 
    			-> exception.authenticationEntryPoint(unauthorizedHandler));
        
        //adds authenticationJwtTokenFilter as a custom filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), 
        		UsernamePasswordAuthenticationFilter.class);
        
       // http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
		// http.formLogin(Customizer.withDefaults());   
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}
	
//	//global CORS Configuration
//	 @Bean
//	    CorsConfigurationSource corsConfigurationSource() {
//	        CorsConfiguration corsConfig = new CorsConfiguration();
//	        // Allow specific origins
//	        //corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//	        corsConfig.setAllowedOrigins(Arrays.asList(frontendUrl));
//	     
//	        // Allow specific HTTP methods
//	        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//	        // Allow specific headers
//	        corsConfig.setAllowedHeaders(Arrays.asList("*"));
//	        // Allow credentials (cookies, authorization headers)
//	        corsConfig.setAllowCredentials(true);
//	        corsConfig.setMaxAge(3600L);
//	        
////	        System.out.println("CORS Configuration:");
////	        System.out.println("Allowed Origins: " + corsConfig.getAllowedOrigins());
////	        System.out.println("Allowed Methods: " + corsConfig.getAllowedMethods());
////	        System.out.println("Allowed Headers: " + corsConfig.getAllowedHeaders());
//
//	        
//	        // Define allowed paths (for all paths use "/**")
//	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        source.registerCorsConfiguration("/**", corsConfig); // Apply to all endpoints
//	        return source;
//	    }

}
