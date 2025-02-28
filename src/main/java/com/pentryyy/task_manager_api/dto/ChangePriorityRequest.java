package com.pentryyy.task_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на изменение приоритета задачи")
public class ChangePriorityRequest {

    @Schema(
        description = "Приоритет задачи. Должен содержать только цифры, заглавные буквы и символ подчеркивания.",
        example = "HIGH",
        maxLength = 50
    )
    @NotBlank(message = "Приоритет не может быть пустым")
    @Size(max = 50, message = "Приоритет не может превышать 50 символов")
    @Pattern(
        regexp = "^[A-Z0-9_]+$",
        message = "Приоритет должен содержать только цифры и заглавные буквы"
    )
    private String priority;
}
