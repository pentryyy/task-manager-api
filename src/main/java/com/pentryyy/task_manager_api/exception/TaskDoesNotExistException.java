package com.pentryyy.task_manager_api.exception;

public class TaskDoesNotExistException extends RuntimeException {
    public TaskDoesNotExistException(Long id){
        super("Задача с id " + id + " не найдена");
    }
}
