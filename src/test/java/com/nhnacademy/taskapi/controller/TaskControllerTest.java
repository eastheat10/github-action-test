package com.nhnacademy.taskapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.dto.request.tag.CreateTagRequest;
import com.nhnacademy.taskapi.dto.request.task.CreateTaskRequest;
import com.nhnacademy.taskapi.dto.request.task.ModifyTaskRequest;
import com.nhnacademy.taskapi.exception.TagNotfoundException;
import com.nhnacademy.taskapi.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("업무 생성")
    void testCreateTask() throws Exception {

        CreateTaskRequest createRequest = new CreateTaskRequest();

        ReflectionTestUtils.setField(createRequest, "projectId", 1L);
        ReflectionTestUtils.setField(createRequest, "title", "task title");
        ReflectionTestUtils.setField(createRequest, "content", "task content");
        ReflectionTestUtils.setField(createRequest, "registrantName", "admin");

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(createRequest);

        log.info("json request: {}", jsonRequest);

        mockMvc.perform(post("/tasks")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isCreated());

        verify(taskService, times(1)).createTask(any(createRequest.getClass()));
    }

    @Test
    @DisplayName("업무 조회")
    void findTask() throws Exception {

        Long taskId = 1L;

        mockMvc.perform(get("/tasks/{id}", taskId)
                   .accept(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON));

        verify(taskService, times(1)).findTask(taskId);
    }

    @Test
    @DisplayName("프로젝트의 업무 목록 조회")
    void findTaskList() throws Exception {

        Long projectId = 1L;

        mockMvc.perform(get("/tasks/project/{projectId}", projectId)
                   .accept(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON));

        verify(taskService, times(1)).findTaskByProjectId(projectId);
    }

    @Test
    @DisplayName("업무 수정")
    void modifyTask() throws Exception {

        ModifyTaskRequest modifyRequest = new ModifyTaskRequest();

        ReflectionTestUtils.setField(modifyRequest, "taskId", 1L);
        ReflectionTestUtils.setField(modifyRequest, "title", "task title");
        ReflectionTestUtils.setField(modifyRequest, "content", "task content");

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modifyRequest);

        log.info("json request: {}", jsonRequest);

        mockMvc.perform(put("/tasks")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isOk())
               .andDo(print());

        verify(taskService, times(1)).modifyTask(any(modifyRequest.getClass()));
    }

    @Test
    @DisplayName("업무 삭제")
    void deleteTask() throws Exception {

        Long id = 1L;

        mockMvc.perform(delete("/tasks/{id}", id))
               .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(id);
    }

    @Test
    @DisplayName("Task 잘못 접근")
    void testWrongAccess() throws Exception {

        doThrow(TagNotfoundException.class).when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/{id}", 1L))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("not found")));
    }

    @Test
    @DisplayName("잘못된 양식")
    void testWrongFormat() throws Exception {

        CreateTagRequest createRequest = new CreateTagRequest();

        String jsonRequest = mapper.writerWithDefaultPrettyPrinter()
                                   .writeValueAsString(createRequest);

        mockMvc.perform(post("/tasks")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("valid")));
    }
}