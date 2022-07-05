package com.nhnacademy.taskapi.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.taskapi.dto.request.project.AddProjectMemberRequest;
import com.nhnacademy.taskapi.dto.request.project.CreateProjectRequest;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.repository.ProjectMembersRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMembersRepository projectMembersRepository;

    @Test
    @DisplayName("새로운 프로젝트 생성")
    void testCreateProject() {

        CreateProjectRequest request = new CreateProjectRequest();

        ReflectionTestUtils.setField(request, "adminId", 1L);
        ReflectionTestUtils.setField(request, "adminUsername", "username");
        ReflectionTestUtils.setField(request, "projectName", "project1");

        when(projectRepository.save(any(Project.class))).thenReturn(mock(Project.class));
        when(projectMembersRepository.save(any(ProjectMember.class)))
            .thenReturn(mock(ProjectMember.class));

        projectService.createProject(request);

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(projectMembersRepository, times(1)).save(any(ProjectMember.class));
    }

    @Test
    @DisplayName("프로젝트에 멤버 추가")
    void testAddProjectMember() {

        AddProjectMemberRequest request = new AddProjectMemberRequest();

        ReflectionTestUtils.setField(request, "projectId", 1L);

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(mock(Project.class)));
        when(projectMembersRepository.saveAll(anyCollection())).thenReturn(null);

        projectService.addMembers(request);

        verify(projectRepository, times(1)).findById(anyLong());
        verify(projectMembersRepository, times(1)).saveAll(anyCollection());
    }

    @Test
    @DisplayName("프로젝트 휴면상태 만들기")
    void testMakeDormantProject() {

        Project project = spy(new Project());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.makeDormantProject(1L);

        verify(project, times(1)).makeDormantProject();
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.DORMANT);
    }

    @Test
    @DisplayName("프로젝트 종료")
    void testMakeEndProject() {

        Project project = spy(new Project());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.makeEndProject(1L);

        verify(project, times(1)).makeEndProject();
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.END);
    }
}