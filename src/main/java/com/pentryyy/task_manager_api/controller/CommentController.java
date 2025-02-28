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

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TaskService taskService;

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

    @GetMapping("/get-comment/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        Comment Comment = commentService.findById(id);
        return ResponseEntity.ok(Comment);
    }

    @DeleteMapping("/delete-comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}