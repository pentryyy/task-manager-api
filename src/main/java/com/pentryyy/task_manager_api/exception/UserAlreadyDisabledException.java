package com.pentryyy.task_manager_api.exception;

public class UserAlreadyDisabledException extends RuntimeException {
    public UserAlreadyDisabledException() {
        super("Пользователь уже отключен");
    }
}
