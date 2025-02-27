package com.pentryyy.task_manager_api.exception;

public class UserDoesNotExistException extends RuntimeException  {
    public UserDoesNotExistException(Long id){
        super("Пользователь с id " + id + " не найден");
    }
}
