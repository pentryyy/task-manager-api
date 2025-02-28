package com.pentryyy.task_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на изменение статуса задачи")
public class ChangeStatusRequest {

    @Schema(
        description = "Статус задачи. Должен содержать только цифры, заглавные буквы и символ подчеркивания.",
        example = "IN_PROGRESS",
        maxLength = 50
    )
    @NotBlank(message = "Статус не может быть пустым")
    @Size(max = 50, message = "Статус не может превышать 50 символов")
    @Pattern(
        regexp = "^[A-Z0-9_]+$",
        message = "Статус должен содержать только цифры и заглавные буквы"
    )
    private String status;
}
