package com.nhnacademy.taskapi.repository;

import static org.assertj.core.api.Assertions.*;

import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectMember;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.entity.ThePersonInCharge;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMembersRepository projectMembersRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void findByTaskId() {

        Project project = new Project();

        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "adminUsername", "admin");
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "content", "content");
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());
        Project savedProject = projectRepository.saveAndFlush(project);

        Task task = new Task();
        ReflectionTestUtils.setField(task, "project", savedProject);
        ReflectionTestUtils.setField(task, "title", "title");
        ReflectionTestUtils.setField(task, "content", "content");
        ReflectionTestUtils.setField(task, "registrantName", "registrant");
        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());

        Task savedTask = taskRepository.saveAndFlush(task);


        ProjectMember pm = new ProjectMember(project, (long) 1, "username" + 1);

        ProjectMember save = projectMembersRepository.save(pm);

        ThePersonInCharge p = new ThePersonInCharge(savedTask, save.getId().getMemberId());

        ThePersonInCharge save1 = personRepository.save(p);

        assertThat(save1.getTask().getId()).isEqualTo(savedTask.getId());
    }
}