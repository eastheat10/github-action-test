package com.nhnacademy.taskapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.dto.request.tag.CreateTagRequest;
import com.nhnacademy.taskapi.dto.request.tag.ModifyTagRequest;
import com.nhnacademy.taskapi.exception.TagNotfoundException;
import com.nhnacademy.taskapi.service.TagService;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TagService tagService;

    @Test
    @DisplayName("태그 생성")
    void createTag() throws Exception {

        CreateTagRequest createRequest = new CreateTagRequest();
        ReflectionTestUtils.setField(createRequest, "projectId", 1L);
        ReflectionTestUtils.setField(createRequest, "name", "tag name");

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(createRequest);

        log.info("json request: {}", jsonRequest);

        mockMvc.perform(post("/tags")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isCreated());

        verify(tagService, times(1)).createTag(any(createRequest.getClass()));
    }

    @Test
    @DisplayName("프로젝트의 태그 조회")
    void findTagsByProjectId() throws Exception {

        Long projectId = 1L;

        when(tagService.findTagsByProjectId(projectId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/tags/project/{projectId}", projectId)
                   .accept(APPLICATION_JSON))
               .andExpect(content().contentType(APPLICATION_JSON))
               .andExpect(status().isOk());

        verify(tagService, times(1)).findTagsByProjectId(projectId);
    }

    @Test
    @DisplayName("태그 수정")
    void modifyTag() throws Exception {

        ModifyTagRequest modifyRequest = new ModifyTagRequest();
        ReflectionTestUtils.setField(modifyRequest, "id", 1L);
        ReflectionTestUtils.setField(modifyRequest, "name", "tag name");

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modifyRequest);

        log.info("json request: {}", jsonRequest);

        mockMvc.perform(put("/tags")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isOk());

        verify(tagService, times(1)).modifyTag(any(modifyRequest.getClass()));
    }

    @Test
    @DisplayName("태그 삭제")
    void deleteTag() throws Exception {

        Long tagId = 1L;

        doNothing().when(tagService).deleteTag(tagId);

        mockMvc.perform(delete("/tags/{id}", tagId))
               .andExpect(status().isNoContent());

        verify(tagService, times(1)).deleteTag(tagId);
    }

    @Test
    @DisplayName("Tag 잘못 접근")
    void testWrongAccess() throws Exception {

        doThrow(TagNotfoundException.class).when(tagService).deleteTag(1L);

        mockMvc.perform(delete("/tags/{id}", 1L))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("not found")));
    }

    @Test
    @DisplayName("잘못된 양식")
    void testWrongFormat() throws Exception {

        CreateTagRequest createRequest = new CreateTagRequest();

        String jsonRequest = mapper.writerWithDefaultPrettyPrinter()
                                   .writeValueAsString(createRequest);

        mockMvc.perform(post("/tags")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("valid")));
    }
}