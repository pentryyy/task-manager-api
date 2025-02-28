package com.pentryyy.task_manager_api.service;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pentryyy.task_manager_api.dto.ChangePriorityRequest;
import com.pentryyy.task_manager_api.dto.ChangeStatusRequest;
import com.pentryyy.task_manager_api.dto.TaskUpdateRequest;
import com.pentryyy.task_manager_api.enumeration.RoleType;
import com.pentryyy.task_manager_api.enumeration.TaskPriority;
import com.pentryyy.task_manager_api.enumeration.TaskStatus;
import com.pentryyy.task_manager_api.exception.AccessDeniedException;
import com.pentryyy.task_manager_api.exception.PriorityDoesNotExistException;
import com.pentryyy.task_manager_api.exception.StatusDoesNotExistException;
import com.pentryyy.task_manager_api.exception.TaskDoesNotExistException;
import com.pentryyy.task_manager_api.model.Task;
import com.pentryyy.task_manager_api.model.User;
import com.pentryyy.task_manager_api.repository.TaskRepository;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task request) {
        if (!EnumUtils.isValidEnum(TaskStatus.class, request.getStatus())) {
            throw new StatusDoesNotExistException(request.getStatus());
        }

        if (!EnumUtils.isValidEnum(TaskPriority.class, request.getPriority())) {
            throw new PriorityDoesNotExistException(request.getPriority());
        }

        return taskRepository.save(request);
    }

    public Page<Task> getAllTasks(
        int page, 
        int limit,
        String sortBy, 
        String sortOrder) {
        
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
        return taskRepository.findAll(PageRequest.of(page, limit, sort));
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                             .orElseThrow(() -> new TaskDoesNotExistException(id));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskDoesNotExistException(id);
        }
        
        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, TaskUpdateRequest request) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            return taskRepository.save(task);
        }
        throw new TaskDoesNotExistException(id);
    }

    public Task changeStatus(Long id, ChangeStatusRequest request) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();

            if (!EnumUtils.isValidEnum(TaskStatus.class, request.getStatus())) {
                throw new StatusDoesNotExistException(request.getStatus());
            }

            task.setStatus(request.getStatus());
            return taskRepository.save(task);
        }
        throw new TaskDoesNotExistException(id);
    }

    public Task changePriority(Long id, ChangePriorityRequest request) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();

            if (!EnumUtils.isValidEnum(TaskPriority.class, request.getPriority())) {
                throw new PriorityDoesNotExistException(request.getPriority());
            }

            task.setPriority(request.getPriority());
            return taskRepository.save(task);
        }
        throw new TaskDoesNotExistException(id);
    }

    public Task changeExecutor(Long id, User user) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setExecutor(user);
            return taskRepository.save(task);
        }
        throw new TaskDoesNotExistException(id);
    }

    public void checkAuthority(Long id, User currentUser){
        Task task = findById(id);

        if (currentUser.getRole() == RoleType.ROLE_USER) {
            if (task.getAuthor() != task.getExecutor()) {
                throw new AccessDeniedException("Нет доступа к редактированию этой задачи");
            }
        }
    }
}
