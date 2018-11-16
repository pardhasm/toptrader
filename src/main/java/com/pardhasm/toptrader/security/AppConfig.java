package com.pardhasm.toptrader.security;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.auth0.AuthenticationController;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppConfig extends WebSecurityConfigurerAdapter {

	@Value(value = "${com.auth0.domain}")
	private String domain;
	@Value(value = "${com.auth0.clientId}")
	private String clientId;
	@Value(value = "${com.auth0.clientSecret}")
	private String clientSecret;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/callback", "/login").permitAll().antMatchers("/**").authenticated().and()
				.logout().permitAll();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
	}

	@Bean
	public AuthenticationController authenticationController() throws UnsupportedEncodingException {
		return AuthenticationController.newBuilder(domain, clientId, clientSecret).build();
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

}
