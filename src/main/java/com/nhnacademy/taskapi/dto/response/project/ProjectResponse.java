package com.nhnacademy.taskapi.dto.response.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.taskapi.entity.Project;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ProjectResponse {

    private final Long id;
    private final String name;
    private final String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.status = project.getStatus().getStatus();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
    }
}
