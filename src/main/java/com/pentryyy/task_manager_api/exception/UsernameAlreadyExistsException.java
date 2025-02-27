package com.pentryyy.task_manager_api.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Пользователь с именем - " + username + " уже существует");
    }
}
