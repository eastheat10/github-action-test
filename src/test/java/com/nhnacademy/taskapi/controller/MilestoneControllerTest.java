package com.nhnacademy.taskapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.dto.request.milestone.CreateMileStoneRequest;
import com.nhnacademy.taskapi.dto.request.milestone.ModifyMileStoneRequest;
import com.nhnacademy.taskapi.dto.response.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.entity.Milestone;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.exception.MilestoneNotFoundException;
import com.nhnacademy.taskapi.service.MilestoneService;
import java.time.LocalDate;
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
@WebMvcTest(MilestoneController.class)
class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MilestoneService milestoneService;

    @Test
    @DisplayName("마일스톤 생성")
    void testCreateMilestone() throws Exception {

        CreateMileStoneRequest createRequest = new CreateMileStoneRequest();
        ReflectionTestUtils.setField(createRequest, "projectId", 1L);
        ReflectionTestUtils.setField(createRequest, "name", "milestone name");
        ReflectionTestUtils.setField(createRequest, "startDate", LocalDate.now());

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(createRequest);

        log.info("json request: {}", jsonRequest);

        mockMvc.perform(post("/milestones")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isCreated());

        verify(milestoneService, times(1)).createMileStone(any(createRequest.getClass()));
    }

    @Test
    @DisplayName("프로젝트에 포함된 마일스톤 조회")
    void testFindMilestoneByProjectId() throws Exception {

        Long projectId = 1L;

        when(milestoneService.findMilestoneByProjectId(projectId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/milestones/project/{projectId}", projectId)
                   .accept(APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마일스톤 조회")
    void testFindMilestone() throws Exception {

        Long id = 1L;

        Milestone milestone = spy(new Milestone());
        when(milestone.getProject()).thenReturn(new Project());
        MilestoneResponse milestoneResponse = new MilestoneResponse(milestone);

        when(milestoneService.findMilestone(id)).thenReturn(milestoneResponse);

        mockMvc.perform(get("/milestones/{id}", id)
                   .accept(APPLICATION_JSON))
               .andExpect(content().contentType(APPLICATION_JSON))
               .andExpect(status().isOk());

        verify(milestoneService, times(1)).findMilestone(id);
    }

    @Test
    @DisplayName("마일스톤 수정")
    void testModifyMilestone() throws Exception {

        ModifyMileStoneRequest modifyRequest = new ModifyMileStoneRequest();

        ReflectionTestUtils.setField(modifyRequest, "id", 1L);
        ReflectionTestUtils.setField(modifyRequest, "name", "milestone name");
        ReflectionTestUtils.setField(modifyRequest, "startDate", LocalDate.now());
        ReflectionTestUtils.setField(modifyRequest, "endDate", LocalDate.now().plusDays(1L));

        String jsonRequest =
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modifyRequest);

        doNothing().when(milestoneService).modifyMilestone(any(ModifyMileStoneRequest.class));

        mockMvc.perform(put("/milestones")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isOk())
            .andDo(print());

        verify(milestoneService, times(1)).modifyMilestone(any(modifyRequest.getClass()));
    }

    @Test
    @DisplayName("마일스톤 삭제")
    void testDeleteMilestone() throws Exception {

        doNothing().when(milestoneService).deleteMilestone(anyLong());

        mockMvc.perform(delete("/milestones/{id}", 1L))
               .andExpect(status().isNoContent());

        verify(milestoneService, times(1)).deleteMilestone(1L);
    }

    @Test
    @DisplayName("Milestone 잘못 접근")
    void testWrongAccess() throws Exception {

        doThrow(MilestoneNotFoundException.class).when(milestoneService).deleteMilestone(1L);

        mockMvc.perform(delete("/milestones/{id}", 1L))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("not found")));
    }

    @Test
    @DisplayName("잘못된 양식")
    void testWrongFormat() throws Exception {

        CreateMileStoneRequest createRequest = new CreateMileStoneRequest();

        String jsonRequest = mapper.writerWithDefaultPrettyPrinter()
                                   .writeValueAsString(createRequest);

        mockMvc.perform(post("/milestones")
                   .contentType(APPLICATION_JSON)
                   .content(jsonRequest))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.type", equalTo("valid")));
    }
}