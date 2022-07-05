package com.nhnacademy.taskapi.repository;

import static org.assertj.core.api.Assertions.*;

import com.nhnacademy.taskapi.entity.Milestone;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.exception.MilestoneNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MilestoneRepositoryTest {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("프로젝트에 해당하는 마일스톤 목록 조회")
    void testMilestoneList() {

        Project project = new Project();

        ReflectionTestUtils.setField(project, "id", 99L);
        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "adminUsername", "admin");
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "content", "project content");
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

        Project savedProject = projectRepository.save(project);

        List<Milestone> list = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Milestone milestone = new Milestone();
            ReflectionTestUtils.setField(milestone, "project", savedProject);
            ReflectionTestUtils.setField(milestone, "name", "milestone" + i);
            ReflectionTestUtils.setField(milestone, "startDate", LocalDate.now().minusDays(i));
            list.add(milestone);
        }

        milestoneRepository.saveAll(list);

        System.out.println("\n==== SELECT ====\n");

        List<Milestone> milestones =
            milestoneRepository.findMilestoneByProject_Id(savedProject.getId());

        milestones.forEach(m -> assertThat(m.getProject().getId()).isEqualTo(savedProject.getId()));
    }

    @Test
    @DisplayName("마일스톤 조회")
    void testMilestone() {

        Project project = new Project();

        ReflectionTestUtils.setField(project, "id", 99L);
        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "adminUsername", "admin");
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "content", "content");
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

        Project savedProject = projectRepository.save(project);

        Milestone milestone = new Milestone();
        ReflectionTestUtils.setField(milestone, "project", savedProject);
        ReflectionTestUtils.setField(milestone, "name", "milestone");
        ReflectionTestUtils.setField(milestone, "startDate", LocalDate.now());

        Milestone savedMilestone = milestoneRepository.saveAndFlush(milestone);

        System.out.println("\n==== SELECT ====\n");

        Milestone findMilestone = milestoneRepository.findById(savedMilestone.getId())
                                                     .orElseThrow(MilestoneNotFoundException::new);

        assertThat(findMilestone.getId()).isEqualTo(savedMilestone.getId());
    }
}