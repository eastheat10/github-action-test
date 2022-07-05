package com.nhnacademy.taskapi.entity;

import com.nhnacademy.taskapi.dto.request.milestone.CreateMileStoneRequest;
import com.nhnacademy.taskapi.dto.request.milestone.ModifyMileStoneRequest;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Milestones")
@Getter
@NoArgsConstructor
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Milestone(Project project, CreateMileStoneRequest request) {
        this.project = project;
        this.name = request.getName();
        this.startDate = LocalDate.now();
        this.endDate = request.getEndDate();
    }

    public void modifyMilestone(ModifyMileStoneRequest modifyRequest) {
        this.name = modifyRequest.getName();
        this.startDate = modifyRequest.getStartDate();
        this.endDate = modifyRequest.getEndDate();
    }
}
