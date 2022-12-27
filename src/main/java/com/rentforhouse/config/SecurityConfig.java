package com.rentforhouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rentforhouse.config.jwt.AuthEntryPointJwt;
import com.rentforhouse.config.jwt.AuthtokenFilter;
import com.rentforhouse.service.impl.userdetail.UserDetailsServiceImpl;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl detailsServiceImpl;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	AuthtokenFilter authenticationJwtTokenFilter() {
		return new AuthtokenFilter();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailsServiceImpl).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors();
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.authorizeRequests().anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/swagger-ui.html/**").antMatchers("/swagger-ui.html#/**")
				.antMatchers("/webjars/springfox-swagger-ui/**").antMatchers("/swagger-resources/**")
				.antMatchers("/v2/api-docs").antMatchers("/api/auth/**").antMatchers("/api/file/**").antMatchers("/api/dasdboard/**")
				.antMatchers(HttpMethod.GET, "/api/houses", "/api/houses/top-5", "/api/houses/status/{trueOrfalse}",
						"/api/houses/{id}", "/api/houses/export/excel", "/api/houses/typeId/{typeId}", "/api/houseTypes", "/api/comments/**")
				.antMatchers(HttpMethod.PUT, "/api/houses/viewPlus/{id}");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}