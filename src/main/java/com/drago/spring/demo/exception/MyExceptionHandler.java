package com.drago.spring.demo.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MyExceptionHandler {

	private static final String ERROR = "error";

	@ExceptionHandler(value = { Exception.class })
	public ModelAndView handleAnyException(Exception ex, WebRequest request) {
		ModelAndView model = new ModelAndView();
		ErrorMessage error = new ErrorMessage(ex.getMessage(), "");
		model.addObject(ERROR, error);
		model.setViewName(ERROR);
		return model;
	}

	@ExceptionHandler(value = { InvalidUserException.class, EmailExistsException.class, NoSuchMarkerException.class })
	public ModelAndView handleUserExceptions(Exception ex, WebRequest request) {
		ModelAndView model = new ModelAndView();
		ErrorMessage error = new ErrorMessage(ex.getMessage(), "");
		model.addObject(ERROR, error);
		model.setViewName(ERROR);
		return model;
	}

	@ExceptionHandler(value = StorageException.class)
	public ModelAndView handleStorageExceptions(Exception ex, WebRequest request) {
		ModelAndView model = new ModelAndView();
		ErrorMessage error = new ErrorMessage(ex.getMessage(), "");
		model.addObject(ERROR, error);
		model.setViewName(ERROR);
		return model;
	}

}
