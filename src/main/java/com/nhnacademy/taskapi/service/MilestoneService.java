package com.nhnacademy.taskapi.service;

import static java.util.stream.Collectors.*;

import com.nhnacademy.taskapi.dto.request.milestone.CreateMileStoneRequest;
import com.nhnacademy.taskapi.dto.request.milestone.ModifyMileStoneRequest;
import com.nhnacademy.taskapi.dto.response.milestone.MilestoneListResponse;
import com.nhnacademy.taskapi.dto.response.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.entity.Milestone;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.exception.MilestoneNotFoundException;
import com.nhnacademy.taskapi.exception.ProjectNotFoundException;
import com.nhnacademy.taskapi.repository.MilestoneRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void createMileStone(CreateMileStoneRequest mileStoneRequest) {

        Project project = projectRepository.findById(mileStoneRequest.getProjectId())
                                           .orElseThrow(ProjectNotFoundException::new);

        milestoneRepository.save(new Milestone(project, mileStoneRequest));
    }

    public List<MilestoneResponse> findMilestoneByProjectId(Long projectId) {

        return milestoneRepository.findMilestoneByProject_Id(projectId)
                                  .stream()
                                  .map(MilestoneResponse::new)
                                  .collect(toList());

    }

    public MilestoneResponse findMilestone(Long id) {

        Milestone milestone =
            milestoneRepository.findById(id)
                               .orElseThrow(MilestoneNotFoundException::new);

        return new MilestoneResponse(milestone);
    }

    @Transactional
    public void modifyMilestone(ModifyMileStoneRequest modifyMileStoneRequest) {

        Milestone milestone = milestoneRepository.findById(modifyMileStoneRequest.getId())
                                                 .orElseThrow(MilestoneNotFoundException::new);

        milestone.modifyMilestone(modifyMileStoneRequest);
    }

    @Transactional
    public void deleteMilestone(Long id) {

        Milestone milestone = milestoneRepository.findById(id)
                                                 .orElseThrow(MilestoneNotFoundException::new);

        milestoneRepository.delete(milestone);
    }
}
