package com.qs.telotengo.product.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.qs.telotengo.product.dto.ErrorDto;
import com.qs.telotengo.product.exception.ValidationExceptions;

@ControllerAdvice
public class ErrorController {

	@ExceptionHandler(ValidationExceptions.class)
	public HttpEntity<ErrorDto> handleValidations(ValidationExceptions e) {
		
		ErrorDto error =new ErrorDto();
		error.setInternalCode(e.getCodError());
		error.setMessage(e.getMessage());
		
		return new ResponseEntity<ErrorDto>(error, e.getStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public HttpEntity<ErrorDto> handleValidationExceptions(
	        MethodArgumentNotValidException ex) {
				ErrorDto error =new ErrorDto();
				error.setMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage().toUpperCase());
				error.setInternalCode("1515");
				return new ResponseEntity<ErrorDto>(error,HttpStatus.BAD_REQUEST);
			}
}
