package com.pentryyy.task_manager_api.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String errorMessage) {
        super(errorMessage);
    }
}
