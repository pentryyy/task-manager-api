package com.pentryyy.task_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на изменение исполнителя задачи")
public class ChangeExecutorRequest {

    @Schema(
        description = "ID исполнителя задачи. Это уникальный идентификатор пользователя, который будет назначен на задачу.",
        example = "1",
        required = true
    )
    @NotNull(message = "ID исполнителя не может быть пустым")
    private Long executor;
}
