package com.nhnacademy.taskapi.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.taskapi.dto.request.tag.CreateTagRequest;
import com.nhnacademy.taskapi.dto.request.tag.ModifyTagRequest;
import com.nhnacademy.taskapi.dto.response.tag.TagResponse;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.entity.Tag;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import com.nhnacademy.taskapi.repository.TagRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("태그 생성")
    void testCreateTag() {

        CreateTagRequest request = new CreateTagRequest();

        ReflectionTestUtils.setField(request, "projectId", 1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(mock(Project.class)));
        when(tagRepository.save(any(Tag.class))).thenReturn(mock(Tag.class));

        tagService.createTag(request);

        verify(projectRepository, times(1)).findById(anyLong());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    @DisplayName("프로젝트에 해당하는 태그 조회")
    void testFindTagsByProject() {

        Long projectId = 1L;

        Project project = new Project();

        ReflectionTestUtils.setField(project, "id", projectId);
        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

        List<Tag> list = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Tag tag = new Tag();
            ReflectionTestUtils.setField(tag, "project", project);
            ReflectionTestUtils.setField(tag, "name", "tag" + i);
            list.add(tag);
        }

        when(tagRepository.findTagsByProject_id(projectId)).thenReturn(list);

        List<TagResponse> tags = tagService.findTagsByProjectId(projectId);

        assertThat(tags.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("태그 수정")
    void testModifyTag() {

        ModifyTagRequest request = new ModifyTagRequest();
        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request,"name", "tag1");

        Tag tag = spy(new Tag());

        when(tagRepository.findById(request.getId())).thenReturn(Optional.of(tag));

        tagService.modifyTag(request);

        verify(tagRepository, times(1)).findById(request.getId());
        verify(tag, times(1)).modifyTag(request.getName());
        assertThat(tag.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("태그 삭제")
    void testDeleteTag() {

        Long tagId = 1L;

        Tag tag = new Tag();

        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).delete(tag);

        tagService.deleteTag(tagId);

        verify(tagRepository, times(1)).findById(tagId);
        verify(tagRepository, times(1)).delete(tag);
    }
}