package com.pentryyy.task_manager_api.exception;

public class StatusDoesNotExistException extends RuntimeException  {
    public StatusDoesNotExistException(String status){
        super("Статус названием " + status + " не найден");
    }
}
