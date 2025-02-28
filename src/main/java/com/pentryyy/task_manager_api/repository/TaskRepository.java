package com.pentryyy.task_manager_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentryyy.task_manager_api.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
