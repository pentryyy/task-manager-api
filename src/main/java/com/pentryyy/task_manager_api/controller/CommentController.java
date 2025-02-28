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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentryyy.task_manager_api.dto.CommentCreateRequest;
import com.pentryyy.task_manager_api.model.Comment;
import com.pentryyy.task_manager_api.model.Task;
import com.pentryyy.task_manager_api.model.User;
import com.pentryyy.task_manager_api.service.CommentService;
import com.pentryyy.task_manager_api.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/comments")
@Tag(name = "Комментарии", description = "API для управления комментариями")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TaskService taskService;

    @Operation(
        summary = "Создать комментарий",
        description = "Добавляет новый комментарий к указанной задаче. Доступно только авторизованным пользователям."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Комментарий успешно создан",
            content = @Content(mediaType = "application/json",
            schema = @Schema(example = "{ \"id\": 1 }"))),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
        @ApiResponse(responseCode = "403", description = "Отсутствие прав на выполнение операции",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "{ \"error\": \"Нет доступа к редактированию этой задачи\" }"))),
        @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PostMapping("/create-comment")
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentCreateRequest request) {
        Task task = taskService.findById(request.getTaskId());
        
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();
        
        User currentUser = (User) authentication.getPrincipal();

        commentService.checkAuthority(task, currentUser);

        Comment Comment = commentService.createComment(request, task);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", Comment.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }

    @Operation(
        summary = "Получить все комментарии",
        description = "Возвращает страницу комментариев с возможностью сортировки и пагинации."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение списка комментариев"),
        @ApiResponse(responseCode = "403", description = "Отсутствие прав на выполнение операции",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "{ \"error\": \"Нет доступа к редактированию этой задачи\" }")))
    })
    @GetMapping("/get-all-comments")
    public ResponseEntity<Page<Comment>> getAllComments(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        Page<Comment> Comments = commentService.getAllComments(
            page, 
            limit, 
            sortBy, 
            sortOrder);
        return ResponseEntity.ok(Comments);
    }

    @Operation(
        summary = "Получить комментарий по ID",
        description = "Возвращает комментарий по указанному идентификатору."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Комментарий найден"),
        @ApiResponse(responseCode = "403", description = "Отсутствие прав на выполнение операции",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "{ \"error\": \"Нет доступа к редактированию этой задачи\" }"))),
        @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @GetMapping("/get-comment/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        Comment Comment = commentService.findById(id);
        return ResponseEntity.ok(Comment);
    }

    @Operation(
        summary = "Удалить комментарий",
        description = "Удаляет комментарий по ID. Только пользователи с соответствующими правами могут удалять комментарии."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Комментарий успешно удален"),
        @ApiResponse(responseCode = "403", description = "Отсутствие прав на выполнение операции",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "{ \"error\": \"Нет доступа к редактированию этой задачи\" }"))),
        @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @DeleteMapping("/delete-comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}