package com.pentryyy.task_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление задачи, содержащий заголовок и описание")
public class TaskUpdateRequest {

    @Schema(description = "Заголовок задачи", example = "Новая задача", maxLength = 255)
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(max = 255, message = "Заголовок не может превышать 255 символов")
    private String title;

    @Schema(description = "Описание задачи", example = "Подробное описание задачи", maxLength = 500)
    @Size(max = 500, message = "Описание не может превышать 500 символов")
    private String description;
}
