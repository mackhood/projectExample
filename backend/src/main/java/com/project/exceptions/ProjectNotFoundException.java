package com.project.exceptions;

public class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException(String value){
        super(String.format("project %s is invalid, try another one",value));
    }
}
