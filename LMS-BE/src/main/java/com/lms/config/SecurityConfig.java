package com.lms.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.lms.serviceImpl.LearnerUserUds;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jfl;

	@Bean
	SecurityFilterChain sfc(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());
		http.cors(cor -> cor.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				List<String> listoforigin = List.of("http://localhost:3000/");
				List<String> listofmethods = List.of("GET", "POST", "PUT", "DELETE");
				List<String> listofheaders = List.of("*");
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(listoforigin);
				cfg.setAllowedMethods(listofmethods);
				cfg.setAllowedHeaders(listofheaders);
			//	cfg.setAllowCredentials(true);
				return cfg;
			}
		}));

		http.authorizeHttpRequests(

				auth ->

				{
					auth.requestMatchers("/user/api1", "/user/signup").authenticated();
					auth.requestMatchers("/user/login", "user/jwt").permitAll();

				}).sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(ap()).addFilterBefore((Filter) jfl, UsernamePasswordAuthenticationFilter.class);

		;
		http.formLogin(forn -> forn.loginPage("/user/login")).httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	UserDetailsService uds() {
		return new LearnerUserUds();
	}

	@Bean
	AuthenticationProvider ap() {
		DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
		dap.setUserDetailsService(uds());
		dap.setPasswordEncoder(pe());

		return dap;
	}

	@Bean
	PasswordEncoder pe() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager am(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
