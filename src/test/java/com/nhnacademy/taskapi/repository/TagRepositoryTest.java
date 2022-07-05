package com.nhnacademy.taskapi.repository;

import static org.assertj.core.api.Assertions.*;

import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.entity.Tag;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findTagsByProjectId() {

        Project project = new Project();

        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "adminUsername", "admin");
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "content", "project content");
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

        Project savedProject = projectRepository.save(project);

        List<Tag> list = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Tag tag = new Tag();
            ReflectionTestUtils.setField(tag, "project", savedProject);
            ReflectionTestUtils.setField(tag, "name", "tag" + i);
            list.add(tag);
        }

        tagRepository.saveAll(list);

        System.out.println("======");

        List<Tag> tag = tagRepository.findTagsByProject_id(savedProject.getId());

        tag.forEach(t -> assertThat(t.getProject().getId()).isEqualTo(project.getId()));
        assertThat(tag.size()).isEqualTo(10);
    }
}