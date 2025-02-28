package com.pentryyy.task_manager_api.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pentryyy.task_manager_api.dto.PasswordChangeRequest;
import com.pentryyy.task_manager_api.dto.RoleUpdateRequest;
import com.pentryyy.task_manager_api.dto.UserUpdateRequest;
import com.pentryyy.task_manager_api.model.User;
import com.pentryyy.task_manager_api.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    
    @Mock
    private UserService userService;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserController userController;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
    }
    
    @Test
    void getAllUsers_ShouldReturnUsersPage() {
        System.out.println("Запуск теста getAllUsers");
        Page<User> usersPage = new PageImpl<>(Collections.singletonList(testUser));
        when(userService.getAllUsers(anyInt(), anyInt(), anyString(), anyString())).thenReturn(usersPage);
        ResponseEntity<Page<User>> response = userController.getAllUsers(0, 10, "id", "ASC");
        
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(userService, times(1)).getAllUsers(0, 10, "id", "ASC");
    }
    
    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.findById(1L)).thenReturn(testUser);
        
        ResponseEntity<?> response = userController.getUserById(1L);
        
        assertNotNull(response.getBody());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).findById(1L);
    }
    
    @Test
    void changeRole_ShouldUpdateUserRole() {
        RoleUpdateRequest request = new RoleUpdateRequest();
        request.setRolename("ADMIN");
        
        ResponseEntity<?> response = userController.changeRole(1L, request);
        
        assertEquals(200, response.getStatusCode().value());
        verify(userService, times(1)).changeRole(1L, "ADMIN");
    }
    
    @Test
    void disableUser_ShouldReturnSuccessMessage() {
        ResponseEntity<?> response = userController.disableUser(1L);
        
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Пользователь отключен"));
        verify(userService, times(1)).disableUser(1L);
    }
    
    @Test
    void enableUser_ShouldReturnSuccessMessage() {
        ResponseEntity<?> response = userController.enableUser(1L);
        
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Пользователь активен"));
        verify(userService, times(1)).enableUser(1L);
    }
    
    @Test
    void updateUser_ShouldReturnSuccessMessage() {
        UserUpdateRequest request = new UserUpdateRequest();
        ResponseEntity<?> response = userController.updateUser(1L, request);
        
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Данные пользователя обновлены"));
        verify(userService, times(1)).updateUser(1L, request);
    }
    
    @Test
    void changePassword_ShouldReturnSuccessMessage() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setPassword("new_password");
        
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        
        ResponseEntity<?> response = userController.changePassword(1L, request);
        
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Пароль успешно обновлен"));
        verify(userService, times(1)).changePassword(1L, "hashed_password");
    }
}
