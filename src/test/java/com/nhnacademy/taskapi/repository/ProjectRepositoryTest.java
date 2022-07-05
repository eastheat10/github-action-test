package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMembersRepository projectMemberRepository;

    @Test
    @DisplayName("username으 프로젝트 조회")
    void findProjectByMemberId() {

        Long memberId = 1L;
        String username = "username1";

        for (int i = 0; i < 10; i++) {

            Project project = new Project();

            ReflectionTestUtils.setField(project, "name", "name" + i);
            ReflectionTestUtils.setField(project, "adminId", 99L);
            ReflectionTestUtils.setField(project, "adminUsername", "admin");
            ReflectionTestUtils.setField(project, "name", "project name");
            ReflectionTestUtils.setField(project, "content", "content");
            ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
            ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

            Project savedProject = projectRepository.save(project);

            ProjectMember projectMember = new ProjectMember(savedProject, memberId, username);
            projectMemberRepository.save(projectMember);
        }

        List<Project> projects = projectRepository.findProjectByUsername(username);

        Assertions.assertThat(projects.size()).isEqualTo(10);
    }
}