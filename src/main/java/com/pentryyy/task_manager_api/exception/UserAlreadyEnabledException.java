package com.pentryyy.task_manager_api.exception;

public class UserAlreadyEnabledException extends RuntimeException {
    public UserAlreadyEnabledException() {
        super("Пользователь уже активен");
    }
}
