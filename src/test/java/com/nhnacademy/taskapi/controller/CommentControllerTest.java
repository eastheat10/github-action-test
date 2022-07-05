package com.nhnacademy.taskapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.dto.request.comment.CreateCommentRequest;
import com.nhnacademy.taskapi.dto.request.comment.ModifyCommentRequest;
import com.nhnacademy.taskapi.exception.CommentNotFoundException;
import com.nhnacademy.taskapi.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("댓글 추가")
    void testCreateComment() throws Exception {

        CreateCommentRequest createRequest = new CreateCommentRequest();

        ReflectionTestUtils.setField(createRequest, "taskId", 1L);
        ReflectionTestUtils.setField(createRequest, "username", "writer");
        ReflectionTestUtils.setField(createRequest, "comment", "hello world");

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(createRequest);

        mockMvc.perform(post("/comments")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isCreated());

        verify(commentService, times(1)).addComment(any(createRequest.getClass()));
    }

    @Test
    @DisplayName("댓글 수정")
    void testModifyComment() throws Exception {

        ModifyCommentRequest modifyRequest = new ModifyCommentRequest();

        ReflectionTestUtils.setField(modifyRequest, "id", 1L);
        ReflectionTestUtils.setField(modifyRequest, "comment", "modify comment");

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modifyRequest);

        mockMvc.perform(put("/comments")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isOk());

        verify(commentService, times(1)).modifyComment(any(modifyRequest.getClass()));
    }

    @Test
    @DisplayName("댓글 삭제")
    void testDeleteComment() throws Exception {

        Long commentId = 1L;

        mockMvc.perform(delete("/comments/{id}", commentId))
               .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(commentId);
    }

    @Test
    @DisplayName("Comment 잘못 접근")
    void testWrongAccess() throws Exception {

        doThrow(CommentNotFoundException.class).when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/comments/{id}", 1L))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("not found")));
    }

    @Test
    @DisplayName("잘못된 양식")
    void testWrongFormat() throws Exception {

        CreateCommentRequest createRequest = new CreateCommentRequest();

        String jsonRequest = mapper.writerWithDefaultPrettyPrinter()
                                   .writeValueAsString(createRequest);

        mockMvc.perform(post("/comments")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("valid")));
    }
}