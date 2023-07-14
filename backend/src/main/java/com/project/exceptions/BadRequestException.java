package com.project.exceptions;

public class BadRequestException extends Exception {
    public BadRequestException(){
        super(String.format("the request  is invalid,you are missing a param"));
    }
}