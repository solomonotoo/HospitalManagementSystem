package com.hms;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hms.exceptions.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	//for logging the errors
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	
	//for handling generic errors
	@ExceptionHandler(Exception.class) //specifies the exact type of error that should be thrown or catch
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //specify the error response status code. in this case is internal server error
	@ResponseBody //indicate that the return value of this method will be directly be bound to the web request
	public ErrorDTO handleGenericException(HttpServletRequest request, Exception ex) {
		
		//create new ErrorDTO object
		ErrorDTO error = new ErrorDTO();
		
		//set values for ErrorDTO field
		error.setTimestamp(new Date()); //set the date the error occured
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //return the integer value that represent the error status code
		//error.setErrors(HttpStatus.BAD_REQUEST.getReasonPhrase()); //get the error message
		
		//setErrors change to addErrors because in ErrorDTO.java String errors is changed to List<String> errors = new ArrayList<>().
		//therefore we no longer use setErrors method but addError method
		error.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		error.setPath(request.getServletPath()); //returns end point url or request path
		
		
		LOGGER.error(ex.getMessage(), ex);
		
		return error;
	}

	
	//handle UserNotFoundException
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorDTO handleUserNotFoundException(HttpServletRequest request, Exception ex) {
		//create a new errorDto object
		ErrorDTO error = new ErrorDTO();
		
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		
		error.addError(ex.getMessage());
		error.setPath(request.getServletPath());  //return the end point url or request path 
		
		LOGGER.error(ex.getMessage(), ex);
		
		return error;
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		//customize the error response
		ErrorDTO error = new ErrorDTO(); //create new errorDTO object
		
		error.setTimestamp(new Date()); //set timestamp
		error.setStatus(HttpStatus.BAD_REQUEST.value()); //set bad request status code
		
		//set path of the request. get the servlet path from the web request
		error.setPath(((ServletWebRequest) request).getRequest().getServletPath());
		
		//get the error message for each field in the request body in a list
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		
		//iterate through each field error in the list
		fieldErrors.forEach(fieldError ->{
			//add error message of each field error to the errorDTO object
			error.addError(fieldError.getDefaultMessage());
		});
		
		return new ResponseEntity<>(error,headers,status);
		
	}
	
	
	
	
}