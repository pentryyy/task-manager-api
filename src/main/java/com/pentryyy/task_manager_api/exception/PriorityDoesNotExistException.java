package com.pentryyy.task_manager_api.exception;

public class PriorityDoesNotExistException extends RuntimeException  {
    public PriorityDoesNotExistException(String priority){
        super("Приоритет с названием " + priority + " не найден");
    }
}
