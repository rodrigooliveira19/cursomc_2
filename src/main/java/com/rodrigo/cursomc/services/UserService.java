package com.rodrigo.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.rodrigo.cursomc.security.UserSS;

public class UserService {

	public static UserSS authenticated() {
		try {
			//retorna o usuário logado. 
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		} catch (Exception e) {
			return null; 
		}
	}
}
