package com.pentryyy.task_manager_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.Collections;

import com.pentryyy.task_manager_api.dto.ChangeExecutorRequest;
import com.pentryyy.task_manager_api.dto.ChangePriorityRequest;
import com.pentryyy.task_manager_api.dto.ChangeStatusRequest;
import com.pentryyy.task_manager_api.dto.TaskUpdateRequest;
import com.pentryyy.task_manager_api.model.Task;
import com.pentryyy.task_manager_api.model.User;
import com.pentryyy.task_manager_api.service.TaskService;
import com.pentryyy.task_manager_api.service.UserService;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskController taskController;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
    }

    @Test
    void createTask_ShouldReturnCreatedTaskId() {
        when(taskService.createTask(any(Task.class))).thenReturn(testTask);

        ResponseEntity<?> response = taskController.createTask(testTask);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("\"id\":1"));
        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void getAllTasks_ShouldReturnPagedTasks() {
        Page<Task> tasksPage = new PageImpl<>(Collections.singletonList(testTask));
        when(taskService.getAllTasks(anyInt(), anyInt(), anyString(), anyString())).thenReturn(tasksPage);

        ResponseEntity<Page<Task>> response = taskController.getAllTasks(0, 10, "id", "ASC");

        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(taskService, times(1)).getAllTasks(0, 10, "id", "ASC");
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        when(taskService.findById(1L)).thenReturn(testTask);

        ResponseEntity<?> response = taskController.getTaskById(1L);

        assertNotNull(response.getBody());
        assertEquals(testTask, response.getBody());
        verify(taskService, times(1)).findById(1L);
    }

    @Test
    void deleteTask_ShouldReturnNoContent() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<?> response = taskController.deleteTask(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        when(taskService.updateTask(1L, request)).thenReturn(testTask);
        when(taskService.findById(1L)).thenReturn(testTask);

        ResponseEntity<?> response = taskController.updateTask(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTask, response.getBody());
        verify(taskService, times(1)).updateTask(1L, request);
        verify(taskService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void changeStatus_ShouldReturnUpdatedTask() {
        ChangeStatusRequest request = new ChangeStatusRequest();
        request.setStatus("In Progress");

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        SecurityContextHolder.setContext(securityContext);

        Task testTask = new Task();
        when(taskService.changeStatus(1L, request)).thenReturn(testTask);

        ResponseEntity<?> response = taskController.changeStatus(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTask, response.getBody());

        verify(taskService, times(1)).changeStatus(1L, request);
    }

    @Test
    void changePriority_ShouldReturnUpdatedTask() {
        ChangePriorityRequest request = new ChangePriorityRequest();
        request.setPriority("High");

        when(taskService.changePriority(1L, request)).thenReturn(testTask);
        when(taskService.findById(1L)).thenReturn(testTask);

        ResponseEntity<?> response = taskController.changePriority(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTask, response.getBody());
        verify(taskService, times(1)).changePriority(1L, request);
        verify(taskService, times(1)).findById(1L);
    }

    @Test
    void changeExecutor_ShouldReturnUpdatedTask() {
        ChangeExecutorRequest request = new ChangeExecutorRequest();
        request.setExecutorId(2L);

        User executor = new User();
        executor.setId(2L);

        when(userService.findById(2L)).thenReturn(executor);
        when(taskService.changeExecutor(1L, executor)).thenReturn(testTask);
        when(taskService.findById(1L)).thenReturn(testTask);

        ResponseEntity<?> response = taskController.changeExecutor(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTask, response.getBody());
        verify(userService, times(1)).findById(2L);
        verify(taskService, times(1)).changeExecutor(1L, executor);
        verify(taskService, times(1)).findById(1L);
    }
}
