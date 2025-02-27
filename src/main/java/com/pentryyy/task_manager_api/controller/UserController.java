package com.pentryyy.task_manager_api.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentryyy.task_manager_api.dto.PasswordChangeRequest;
import com.pentryyy.task_manager_api.dto.RoleUpdateRequest;
import com.pentryyy.task_manager_api.dto.UserUpdateRequest;
import com.pentryyy.task_manager_api.model.User;
import com.pentryyy.task_manager_api.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/get-all-users")
    public ResponseEntity<Page<User>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        Page<User> users = userService.getAllUsers(
            page, 
            limit, 
            sortBy, 
            sortOrder);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/change-role/{id}")
    public ResponseEntity<?> changeRole(
        @PathVariable Long id,  
        @RequestBody @Valid RoleUpdateRequest request) {
          
        userService.changeRole(id, request.getRolename());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rolename", request.getRolename())
                  .put("userId", id);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }

    @PatchMapping("/disable-user/{id}")
    public ResponseEntity<?> disableUser(@PathVariable Long id) {
        userService.disableUser(id);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Пользователь отключен");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }

    @PatchMapping("/enable-user/{id}")
    public ResponseEntity<?> enableUser(@PathVariable Long id) {
        userService.enableUser(id);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Пользователь активен");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long id, 
        @RequestBody @Valid UserUpdateRequest request) {   
        
        userService.updateUser(id, request);
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Данные пользователя обновлены");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }

    @PatchMapping("/change-pass/{id}")
    public ResponseEntity<?> changePassword(
        @PathVariable Long id, 
        @RequestBody @Valid PasswordChangeRequest request) {
        
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        userService.changePassword(id, encryptedPassword);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Пароль успешно обновлен");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(jsonObject.toString());
    }
}
