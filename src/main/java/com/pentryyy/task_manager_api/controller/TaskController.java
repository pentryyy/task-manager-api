package com.pentryyy.task_manager_api.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping("/create-task")
    public ResponseEntity<?> createTask(@RequestBody @Valid Task request) {
        Task task = taskService.createTask(request);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", task.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }

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

    @GetMapping("/get-task/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

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

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(
        @PathVariable Long id,
        @RequestBody @Valid ChangeStatusRequest request) {    
            
        taskService.changeStatus(id, request);
        Task updatedTask = taskService.findById(id);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(updatedTask);   
    }

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