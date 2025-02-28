package com.pentryyy.task_manager_api.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentryyy.task_manager_api.dto.ChangeExecutorRequest;
import com.pentryyy.task_manager_api.dto.ChangePriorityRequest;
import com.pentryyy.task_manager_api.dto.ChangeStatusRequest;
import com.pentryyy.task_manager_api.dto.TaskUpdateRequest;
import com.pentryyy.task_manager_api.model.Task;
import com.pentryyy.task_manager_api.model.User;
import com.pentryyy.task_manager_api.service.TaskService;
import com.pentryyy.task_manager_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/tasks")
@Tag(name = "Задачи", description = "Управление задачами")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Создать новую задачу", description = "Создает новую задачу и возвращает её ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Задача успешно создана", 
                     content = @Content(schema = @Schema(implementation = JSONObject.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    })
    @PostMapping("/create-task")
    public ResponseEntity<?> createTask(@RequestBody @Valid Task request) {
        Task task = taskService.createTask(request);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", task.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }

    @Operation(summary = "Получить список всех задач", description = "Возвращает список задач с пагинацией")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешный запрос", 
                     content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/get-all-tasks")
    public ResponseEntity<Page<Task>> getAllTasks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        Page<Task> tasks = taskService.getAllTasks(
            page, 
            limit, 
            sortBy, 
            sortOrder);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по её ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Задача найдена", 
                     content = @Content(schema = @Schema(implementation = Task.class))),
        @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/get-task/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по её ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Задача удалена"),
        @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить заголовок и описание задачи", description = "Обновляет только title и description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Задача обновлена", 
                     content = @Content(schema = @Schema(implementation = TaskUpdateRequest.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PatchMapping("/update-task/{id}")
    public ResponseEntity<?> updateTask(
        @PathVariable Long id,
        @RequestBody @Valid TaskUpdateRequest request) {    
            
        taskService.updateTask(id, request);
        Task updatedTask = taskService.findById(id);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(updatedTask);   
    }

    @Operation(summary = "Изменить статус задачи", description = "Обновляет статус задачи")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус обновлен", 
                     content = @Content(schema = @Schema(implementation = ChangeStatusRequest.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "403", description = "Ошибка доступа")
    })
    @PatchMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(
        @PathVariable Long id,
        @RequestBody @Valid ChangeStatusRequest request) {    
        
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();
        
        User currentUser = (User) authentication.getPrincipal();

        taskService.checkAuthority(id, currentUser);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(taskService.changeStatus(id, request));   
    }

    @Operation(summary = "Изменить приоритет задачи", description = "Обновляет приоритет задачи")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Приоритет обновлен", 
                     content = @Content(schema = @Schema(implementation = ChangePriorityRequest.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PatchMapping("/change-priority/{id}")
    public ResponseEntity<?> changePriority(
        @PathVariable Long id,
        @RequestBody @Valid ChangePriorityRequest request) {    
            
        taskService.changePriority(id, request);
        Task updatedTask = taskService.findById(id);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(updatedTask);   
    }

    @Operation(summary = "Изменить исполнителя задачи", description = "Обновляет исполнителя по ID пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Исполнитель обновлен", 
                     content = @Content(schema = @Schema(implementation = ChangeExecutorRequest.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PatchMapping("/change-executor/{id}")
    public ResponseEntity<?> changeExecutor(
        @PathVariable Long id,
        @RequestBody @Valid ChangeExecutorRequest request) {    
        
        User user = userService.findById(request.getExecutor());
        taskService.changeExecutor(id, user);
        Task updatedTask = taskService.findById(id);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(updatedTask);   
    }
}