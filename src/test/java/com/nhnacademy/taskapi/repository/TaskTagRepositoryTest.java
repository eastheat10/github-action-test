package com.nhnacademy.taskapi.repository;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.entity.Tag;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.entity.TaskTag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TaskTagRepositoryTest {

    @Autowired
    private TaskTagRepository taskTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findByTask_Id() {

        Project project = new Project();

        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "adminUsername", "admin");
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "content", "project content");
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

        Project savedProject = projectRepository.save(project);

        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tag tag = new Tag();
            ReflectionTestUtils.setField(tag, "project", savedProject);
            ReflectionTestUtils.setField(tag, "name", "tag name" + i);
            tagList.add(tag);
        }

        List<Tag> savedTag = tagRepository.saveAll(tagList);

        Task task = new Task();
        ReflectionTestUtils.setField(task, "project", savedProject);
        ReflectionTestUtils.setField(task, "title", "title");
        ReflectionTestUtils.setField(task, "content", "content");
        ReflectionTestUtils.setField(task, "registrantName", "registrant");
        ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        List<TaskTag> collect = savedTag.stream()
                                        .peek(t -> System.out.println(t.getId()))
                                        .map(tag -> new TaskTag(savedTask, tag))
                                        .collect(toList());

        taskTagRepository.saveAll(collect);

        List<TaskTag> taskTags = taskTagRepository.findByTaskId(savedTask.getId());

        taskTags.forEach(tt -> assertThat(tt.getTask().getId()).isEqualTo(savedTask.getId()));
    }

}