package com.rodrigo.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.rodrigo.cursomc.services.DBService;
import com.rodrigo.cursomc.services.EmailService;
import com.rodrigo.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {

	/*@Bean - define o método como componente para o sistema. 
	 * Podendo ser recuperado através do @Autowired em outras classes. 
	 * */
	@Autowired
	private DBService dbService; 
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		dbService.instatiateTestDatabase();
		return true; 
	}
	
	@Bean
	public EmailService emailServices() {
		return new MockEmailService(); 
	}
}
