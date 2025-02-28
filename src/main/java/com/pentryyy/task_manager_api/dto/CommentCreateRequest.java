package com.pentryyy.task_manager_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Запрос на создание комментария")
public class CommentCreateRequest {

    @Schema(description = "Текст комментария", 
            example = "Это тестовый комментарий", 
            maxLength = 2000)
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(max = 2000, message = "Текст комментария не может превышать 2000 символов")
    private String text;

    @Schema(description = "ID задачи, к которой привязывается комментарий", 
            example = "1", 
            required = true)
    @NotNull(message = "ID задачи не может быть null")
    private Long taskId;
}
