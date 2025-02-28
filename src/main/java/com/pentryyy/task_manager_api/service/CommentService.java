package com.pentryyy.task_manager_api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pentryyy.task_manager_api.dto.CommentCreateRequest;
import com.pentryyy.task_manager_api.enumeration.RoleType;
import com.pentryyy.task_manager_api.exception.AccessDeniedException;
import com.pentryyy.task_manager_api.exception.CommentDoesNotExistException;
import com.pentryyy.task_manager_api.model.Comment;
import com.pentryyy.task_manager_api.model.Task;
import com.pentryyy.task_manager_api.model.User;
import com.pentryyy.task_manager_api.repository.CommentRepository;

import org.springframework.data.domain.PageRequest;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(CommentCreateRequest request, Task task) {
        Comment comment = new Comment();
        
        comment.setText(request.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setTask(task);
        return commentRepository.save(comment);
    }

    public Page<Comment> getAllComments(
        int page, 
        int limit,
        String sortBy, 
        String sortOrder) {
        
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
        return commentRepository.findAll(PageRequest.of(page, limit, sort));
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                                .orElseThrow(() -> new CommentDoesNotExistException(id));
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentDoesNotExistException(id);
        }
        
        commentRepository.deleteById(id);
    }

    public void checkAuthority(Task task, User currentUser){
        System.out.println(currentUser.getRole());

        if (currentUser.getRole() == RoleType.ROLE_USER) {
            if (task.getAuthor() != task.getExecutor()) {
                throw new AccessDeniedException("Нет доступа к редактированию этой задачи");
            }
        }
    }
}
