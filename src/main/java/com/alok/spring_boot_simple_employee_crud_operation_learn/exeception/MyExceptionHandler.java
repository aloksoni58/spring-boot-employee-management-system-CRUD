package com.alok.spring_boot_simple_employee_crud_operation_learn.exeception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.alok.spring_boot_simple_employee_crud_operation_learn.response_structure.MyResponseStructure;

@RestControllerAdvice
public class MyExceptionHandler {

	@ExceptionHandler(value = EmployeeAllreadyExistsException.class)
	public String handleEmployeeAllreadyExistsException(EmployeeAllreadyExistsException e) {
		e.printStackTrace();
		return e.getMessage();
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<MyResponseStructure<Map<String, String>>>handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
	
		List<ObjectError> objectErrors = e.getAllErrors();
		Map<String, String> map = new HashMap<>();
		for (ObjectError objectError : objectErrors) {
			FieldError fieldError = (FieldError)objectError;
			String attrubuteName = fieldError.getField();
			String message = fieldError.getDefaultMessage();
			map.put(attrubuteName, message);
		}
		MyResponseStructure<Map<String, String>> myResponseStructure = new MyResponseStructure<>();
		myResponseStructure.setStatusCode(400);
		myResponseStructure.setMsg("Validation failed!!!");
		myResponseStructure.setData(map);
		return ResponseEntity.badRequest().body(myResponseStructure);
	}
	
	@ExceptionHandler(value = HandlerMethodValidationException.class)
	public ResponseEntity<MyResponseStructure<String>> handleMethodValidationException(HandlerMethodValidationException e) {
		MyResponseStructure<String> myResponseStructure = new MyResponseStructure<>();
		myResponseStructure.setStatusCode(400);
		myResponseStructure.setMsg(e.getMessage());
		myResponseStructure.setData(null);
		return ResponseEntity.badRequest().body(myResponseStructure);
	}

}
