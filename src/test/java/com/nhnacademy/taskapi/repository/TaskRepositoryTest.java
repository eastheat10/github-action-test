package com.nhnacademy.taskapi.repository;

import static org.assertj.core.api.Assertions.*;

import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.exception.TaskNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("프로젝트 아이디로 업무 조회")
    void testFindTaskByProjectId() {

        Project project = new Project();

        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "adminUsername", "admin");
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "content", "project content");
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

        Project savedProject = projectRepository.save(project);

        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = new Task();
            ReflectionTestUtils.setField(task, "project", savedProject);
            ReflectionTestUtils.setField(task, "title", "title" + i);
            ReflectionTestUtils.setField(task, "content", "content");
            ReflectionTestUtils.setField(task, "registrantName", "registrantName");
            ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
            taskList.add(task);
        }

        taskRepository.saveAllAndFlush(taskList);

        List<Task> tasks = taskRepository.findByProject_Id(savedProject.getId());

        tasks
            .forEach(task -> assertThat(task.getProject().getId()).isEqualTo(savedProject.getId()));
    }



}