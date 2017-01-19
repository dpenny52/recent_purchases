package com.dpenny.exceptions;


@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException(String username) {
		super("User with username of '" + username + "' was not found");
	}
	
}
