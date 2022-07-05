package com.nhnacademy.taskapi.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import com.nhnacademy.taskapi.dto.request.tag.CreateTagRequest;
import com.nhnacademy.taskapi.dto.request.tag.ModifyTagRequest;
import com.nhnacademy.taskapi.dto.response.tag.TagResponse;
import com.nhnacademy.taskapi.service.TagService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<Void> createTag(@Valid @RequestBody CreateTagRequest createRequest) {

        tagService.createTag(createRequest);

        return ResponseEntity.status(CREATED)
                             .build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TagResponse>> findTagsByProjectId(@PathVariable Long projectId) {

        List<TagResponse> tags = tagService.findTagsByProjectId(projectId);

        return ResponseEntity.status(OK)
                             .contentType(APPLICATION_JSON)
                             .body(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> findTag(@PathVariable Long id) {

        TagResponse tag = tagService.findById(id);

        return ResponseEntity.status(OK)
                             .contentType(APPLICATION_JSON)
                             .body(tag);
    }
    @PutMapping
    public ResponseEntity<Void> modifyTag(@Valid @RequestBody ModifyTagRequest modifyRequest) {

        tagService.modifyTag(modifyRequest);

        return ResponseEntity.status(OK)
                             .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {

        tagService.deleteTag(id);

        return ResponseEntity.status(NO_CONTENT)
                             .build();
    }
}
