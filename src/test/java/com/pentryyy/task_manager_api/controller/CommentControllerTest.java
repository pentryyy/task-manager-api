package com.pentryyy.task_manager_api.controller;

import com.pentryyy.task_manager_api.model.Comment;
import com.pentryyy.task_manager_api.service.CommentService;
import com.pentryyy.task_manager_api.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    @WithMockUser
    void getCommentById_ReturnsComment() throws Exception {
        Comment mockComment = new Comment();
        mockComment.setId(1L);

        when(commentService.findById(1L)).thenReturn(mockComment);

        mockMvc.perform(get("/comments/get-comment/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(commentService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteComment_CommentFound_ReturnsNoContent() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/comments/delete-comment/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(commentService, times(1)).deleteComment(1L);
    }
}
