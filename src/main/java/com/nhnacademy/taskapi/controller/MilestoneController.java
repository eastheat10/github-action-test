package com.nhnacademy.taskapi.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import com.nhnacademy.taskapi.dto.request.milestone.CreateMileStoneRequest;
import com.nhnacademy.taskapi.dto.request.milestone.ModifyMileStoneRequest;
import com.nhnacademy.taskapi.dto.response.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.service.MilestoneService;
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
@RequestMapping("/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @PostMapping
    public ResponseEntity<Void> createMilestone(@Valid @RequestBody
                                                CreateMileStoneRequest createRequest) {

        milestoneService.createMileStone(createRequest);

        return ResponseEntity.status(CREATED)
                             .build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<MilestoneResponse>> findMilestoneByProjectId(
        @PathVariable Long projectId) {

        List<MilestoneResponse> milestones = milestoneService.findMilestoneByProjectId(projectId);

        return ResponseEntity.status(OK)
                             .contentType(APPLICATION_JSON)
                             .body(milestones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MilestoneResponse> findMilestone(@PathVariable Long id) {

        MilestoneResponse milestone = milestoneService.findMilestone(id);

        return ResponseEntity.status(OK)
                             .contentType(APPLICATION_JSON)
                             .body(milestone);
    }

    @PutMapping
    public ResponseEntity<Void> modifyMilestone(
        @Valid @RequestBody ModifyMileStoneRequest modifyRequest) {

        milestoneService.modifyMilestone(modifyRequest);

        return ResponseEntity.status(OK)
                             .build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable Long id) {

        milestoneService.deleteMilestone(id);

        return ResponseEntity.status(NO_CONTENT)
                             .build();
    }
}
