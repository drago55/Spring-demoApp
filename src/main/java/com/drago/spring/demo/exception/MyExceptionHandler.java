package com.drago.spring.demo.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

	private static final String ERROR = "error";

	@ExceptionHandler(value = { Exception.class })
	public ModelAndView handleAnyException(Exception ex, WebRequest request) {
		ModelAndView model = new ModelAndView();
		ErrorMessage error = new ErrorMessage("General exception check log file!", "");
		model.addObject(ERROR, error);
		model.setViewName(ERROR);
		log.error("Greska !", ex);
		return model;
	}

	@ExceptionHandler(value = { InvalidUserException.class, NoSuchMarkerException.class })
	public ModelAndView handleUserExceptions(Exception ex, WebRequest request) {
		ModelAndView model = new ModelAndView();
		ErrorMessage error = new ErrorMessage(ex.getMessage(), "");
		model.addObject(ERROR, error);
		model.setViewName(ERROR);
		log.error("Greska !", ex);
		return model;
	}

	@ExceptionHandler(value = StorageException.class)
	public ModelAndView handleStorageExceptions(Exception ex, WebRequest request) {
		ModelAndView model = new ModelAndView();
		ErrorMessage error = new ErrorMessage(ex.getMessage(), "");
		model.addObject(ERROR, error);
		model.setViewName(ERROR);
		log.error("Greska !", ex);
		return model;
	}

}
