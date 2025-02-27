package com.pentryyy.task_manager_api.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Пользователь с email - " + email + " уже существует");
    }
}
