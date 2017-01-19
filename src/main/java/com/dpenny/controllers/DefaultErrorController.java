package com.dpenny.controllers;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dpenny.exceptions.PathNotFoundException;



@RestController
public class DefaultErrorController implements ErrorController {

		private static final String PATH = "/error";
		
		@RequestMapping(value = PATH)
		public void throwErrorException() {
			throw new PathNotFoundException();
		}
		
		@ExceptionHandler(PathNotFoundException.class) 
		public ResponseEntity<String> pathNotFound(Exception e) {
			return new ResponseEntity<String>("This request path doesn't exist!", HttpStatus.BAD_REQUEST);
		}

		@Override
		public String getErrorPath() {
			return PATH;
		}
}
