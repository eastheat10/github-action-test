package com.nhnacademy.taskapi.service;

import static java.util.stream.Collectors.*;

import com.nhnacademy.taskapi.dto.request.tag.CreateTagRequest;
import com.nhnacademy.taskapi.dto.request.tag.ModifyTagRequest;
import com.nhnacademy.taskapi.dto.response.tag.TagResponse;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.Tag;
import com.nhnacademy.taskapi.exception.ProjectNotFoundException;
import com.nhnacademy.taskapi.exception.TagNotfoundException;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import com.nhnacademy.taskapi.repository.TagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void createTag(CreateTagRequest createTagRequest) {

        Project project = projectRepository.findById(createTagRequest.getProjectId())
                                           .orElseThrow(ProjectNotFoundException::new);

        tagRepository.save(new Tag(project, createTagRequest.getName()));
    }

    public List<TagResponse> findTagsByProjectId(Long projectId) {

        List<Tag> tags = tagRepository.findTagsByProject_id(projectId);
        return tags.stream()
                   .map(TagResponse::new)
                   .collect(toList());
    }

    public TagResponse findById(Long id) {

        Tag tag = tagRepository.findById(id)
                               .orElseThrow(TagNotfoundException::new);

        return new TagResponse(tag);
    }

    @Transactional
    public void modifyTag(ModifyTagRequest modifyTagRequest) {

        Tag tag = tagRepository.findById(modifyTagRequest.getId())
                               .orElseThrow(TagNotfoundException::new);

        tag.modifyTag(modifyTagRequest.getName());
    }

    @Transactional
    public void deleteTag(Long id) {

        Tag tag = tagRepository.findById(id)
                               .orElseThrow(TagNotfoundException::new);

        tagRepository.delete(tag);
    }
}
