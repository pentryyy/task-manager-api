package com.pentryyy.task_manager_api.exception;

public class RoleDoesNotExistException extends RuntimeException  {
    public RoleDoesNotExistException(String rolename){
        super("Роль с названием " + rolename + " не найдена");
    }
}
