package com.pentryyy.task_manager_api.exception;

public class CommentDoesNotExistException extends RuntimeException  {
    public CommentDoesNotExistException(Long id){
        super("Комментарий с id " + id + " не найден");
    }
}
