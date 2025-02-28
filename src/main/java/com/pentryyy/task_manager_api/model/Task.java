package com.pentryyy.task_manager_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(max = 255, message = "Заголовок не может превышать 255 символов")
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Size(max = 500, message = "Описание не может превышать 500 символов")
    @Column(name = "description", length = 500)
    private String description;

    @NotBlank(message = "Статус не может быть пустым")
    @Size(max = 50, message = "Статус не может превышать 50 символов")
    @Pattern(
        regexp = "^[A-Z0-9_]+$",
        message = "Статус должен содержать только цифры и заглавные буквы"
    )
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @NotBlank(message = "Приоритет не может быть пустым")
    @Size(max = 50, message = "Приоритет не может превышать 50 символов")
    @Pattern(
        regexp = "^[A-Z0-9_]+$",
        message = "Приоритет должен содержать только цифры и заглавные буквы"
    )
    @Column(name = "priority", nullable = false, length = 50)
    private String priority;
    
    @NotNull(message = "Автор задачи не может быть null")
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;
}
