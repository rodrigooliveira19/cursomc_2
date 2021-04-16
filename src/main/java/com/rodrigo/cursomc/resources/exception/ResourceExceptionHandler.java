package com.rodrigo.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.rodrigo.cursomc.exception.AuthorizationException;
import com.rodrigo.cursomc.exception.DataIntegrityException;
import com.rodrigo.cursomc.exception.FileException;
import com.rodrigo.cursomc.exception.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	//@ExceptionHandler - O spring já fica na escuta para este tipo de exceçao. O Parametro da função recebe a exceção. 
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException ex, HttpServletRequest request){
		
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err); 
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException ex, HttpServletRequest request){
		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err); 
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> dataIntegrity(MethodArgumentNotValidException ex, HttpServletRequest request){
		
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", System.currentTimeMillis());
		for(FieldError fieldError: ex.getBindingResult().getFieldErrors())
			err.addError(fieldError.getField(), fieldError.getDefaultMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err); 
	}
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException ex, HttpServletRequest request){
		
		//FORBIDDEN - código para acesso negado.
		StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), ex.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err); 
	}
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException ex, HttpServletRequest request){
		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err); 
	}
	
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException ex, HttpServletRequest request){
		
		//Pegar o status http da Excecao / AWS Error code. 
		HttpStatus code = HttpStatus.valueOf(ex.getErrorCode()); 
		
		StandardError err = new StandardError(code.value(), ex.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(code).body(err); 
	}
	
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazoClient(AmazonClientException ex, HttpServletRequest request){
		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err); 
	}
	
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazoClient(AmazonS3Exception ex, HttpServletRequest request){
		
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err); 
	}

}













