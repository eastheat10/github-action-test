package com.nhnacademy.taskapi.service;

import static java.util.stream.Collectors.*;

import com.nhnacademy.taskapi.dto.request.project.AddProjectMemberRequest;
import com.nhnacademy.taskapi.dto.request.project.CreateProjectRequest;
import com.nhnacademy.taskapi.dto.response.project.ProjectResponse;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.exception.ProjectNotFoundException;
import com.nhnacademy.taskapi.repository.ProjectMembersRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMembersRepository projectMembersRepository;

    @Transactional
    public void createProject(CreateProjectRequest createRequest) {

        Project createdProject = projectRepository.save(new Project(createRequest));

        ProjectMember projectMember =
            new ProjectMember(createdProject, createdProject.getId(), createdProject.getAdminUsername());

        projectMembersRepository.save(projectMember);
    }

    public List<ProjectResponse> findProjectByMemberId(String username) {

        return projectRepository.findProjectByUsername(username)
                                .stream()
                                .map(ProjectResponse::new)
                                .collect(toList());
    }

    public ProjectResponse findProject(Long id) {

        Project project = projectRepository.findById(id)
                                           .orElseThrow(ProjectNotFoundException::new);

        return new ProjectResponse(project);
    }

    @Transactional
    public void addMembers(AddProjectMemberRequest addMemberRequest) {

        Project project = projectRepository.findById(addMemberRequest.getProjectId())
                                           .orElseThrow(ProjectNotFoundException::new);

        List<ProjectMember> projectMembers =
            addMemberRequest.getMemberInfoList()
                            .stream()
                            .map(info -> new ProjectMember(project, info))
                            .collect(toList());

        projectMembersRepository.saveAll(projectMembers);
    }

    @Transactional
    public void makeDormantProject(Long id) {

        Project project = projectRepository.findById(id)
                                           .orElseThrow(ProjectNotFoundException::new);

        project.makeDormantProject();
    }

    @Transactional
    public void makeEndProject(Long id) {

        Project project = projectRepository.findById(id)
                                           .orElseThrow(ProjectNotFoundException::new);

        project.makeEndProject();
    }
}
