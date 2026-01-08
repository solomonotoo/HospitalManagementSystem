package com.hms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorDTO {

	private Date timestamp; //date and time the error occured
	private int status; //error status code
	private String path; //error path or url
	
	//private String errors; //for error message
	private List<String> errors = new ArrayList<>(); //for error message

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

//	public String getErrors() {
//		return errors;
//	}
//
//	public void setErrors(String errors) {
//		this.errors = errors;
//	}
	

	//method that add error to the message
	public void addError(String message) {
		this.errors.add(message);
	}
	
}
