package com.project.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String param) {

        super(String.format("User  not found with param : '%s'", param));
    }



}