package com.project.exceptions;

public class UserAlreadyExistsInProjectException extends Exception {

    public UserAlreadyExistsInProjectException(String username, String projectId) {
        super(String.format("Username %s already exists in project %s, try another one", username, projectId));
    }
}