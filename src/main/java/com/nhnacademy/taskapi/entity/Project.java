package com.nhnacademy.taskapi.entity;

import com.nhnacademy.taskapi.dto.request.project.CreateProjectRequest;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Projects")
@Getter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "admin_username", nullable = false)
    private String adminUsername;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Project(CreateProjectRequest request) {
        this.adminId = request.getAdminId();
        this.adminUsername = request.getAdminUsername();
        this.status = ProjectStatus.PROGRESS;
        this.name = request.getProjectName();
        this.content = request.getContent();
        this.startDate = LocalDate.now();
    }

    public void makeEndProject() {
        this.status = ProjectStatus.END;
    }

    public void makeDormantProject() {
        this.status = ProjectStatus.DORMANT;
    }
}
