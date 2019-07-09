package com.drago.spring.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.csrf().disable();
		httpSecurity.httpBasic().disable();
		httpSecurity.authorizeRequests()
				.antMatchers("/js/**", "/h2-console/**", "/", "/imagePath/**", "/css/**", "/webjars/**", "/errors",
						"/registerUser/**", "index", "/processRegistration/**", "/contact", "/about", "/index",
						"/processLogin/**", "/fullscreen_map")
				.permitAll()
				.antMatchers("/showMarkers/**", "/editMarker", "/deleteMarker", "/imagePath/upload-dir/**",
						"/addMarker", "/showCreateMarker")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_USER").anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll().and().logout().permitAll().logoutSuccessUrl("/index?google_map")
				.invalidateHttpSession(true).clearAuthentication(true);

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

}
