package com.nhnacademy.taskapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.dto.request.project.AddProjectMemberRequest;
import com.nhnacademy.taskapi.dto.request.project.CreateProjectRequest;
import com.nhnacademy.taskapi.exception.ProjectNotFoundException;
import com.nhnacademy.taskapi.service.ProjectService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProjectService projectService;

    @Test
    @DisplayName("프로젝트 생성")
    void testCreateProject() throws Exception {

        CreateProjectRequest createRequest = new CreateProjectRequest();

        ReflectionTestUtils.setField(createRequest, "adminId", 1L);
        ReflectionTestUtils.setField(createRequest, "adminUsername", "admin username");
        ReflectionTestUtils.setField(createRequest, "projectName", "project name");

        String jsonRequest = mapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/projects")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isCreated());

        verify(projectService, times(1)).createProject(any(createRequest.getClass()));
    }

    @Test
    @DisplayName("프로젝트 멤버 추가")
    void testAddMembers() throws Exception {

        List<AddProjectMemberRequest.MemberInfo> memberInfoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            AddProjectMemberRequest.MemberInfo info = new AddProjectMemberRequest.MemberInfo();
            ReflectionTestUtils.setField(info, "memberId", (long) i);
            ReflectionTestUtils.setField(info, "username", "username" + i);
            memberInfoList.add(info);
        }

        AddProjectMemberRequest createRequest = new AddProjectMemberRequest();

        ReflectionTestUtils.setField(createRequest, "projectId", 1L);
        ReflectionTestUtils.setField(createRequest, "memberInfoList", memberInfoList);

        String jsonRequest = mapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/projects/members")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isCreated())
               .andDo(print());

        verify(projectService, times(1)).addMembers(any(createRequest.getClass()));
    }

    @Test
    @DisplayName("멤버가 참여하는 프로젝트 조회")
    void testFindProjectByMemberId() throws Exception {

        when(projectService.findProjectByMemberId(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/projects/members/{username}", "username")
                   .accept(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(APPLICATION_JSON))
               .andDo(print());

        verify(projectService, times(1)).findProjectByMemberId("username");
    }

    @Test
    @DisplayName("휴면 프로젝트 만들기")
    void testMakeDormantProject() throws Exception {

        doNothing().when(projectService).makeDormantProject(anyLong());

        mockMvc.perform(put("/projects/{id}/dormant", 1L))
               .andExpect(status().isOk());

        verify(projectService, times(1)).makeDormantProject(1L);
    }

    @Test
    @DisplayName("종료 프로젝트 만들기")
    void testMakeEndProject() throws Exception {

        doNothing().when(projectService).makeDormantProject(anyLong());

        mockMvc.perform(put("/projects/{id}/end", 1L))
               .andExpect(status().isOk());

        verify(projectService, times(1)).makeEndProject(1L);
    }

    @Test
    @DisplayName("Project 잘못 접근")
    void testWrongAccess() throws Exception {

        doThrow(ProjectNotFoundException.class).when(projectService).makeEndProject(1L);

        mockMvc.perform(put("/projects/{id}/end", 1L))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("not found")));
    }

    @Test
    @DisplayName("잘못된 양식")
    void testWrongFormat() throws Exception {

        CreateProjectRequest createRequest = new CreateProjectRequest();

        String jsonRequest = mapper.writerWithDefaultPrettyPrinter()
                                   .writeValueAsString(createRequest);

        mockMvc.perform(post("/projects")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("valid")));
    }
}