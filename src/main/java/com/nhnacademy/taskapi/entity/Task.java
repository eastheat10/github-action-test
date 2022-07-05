package com.nhnacademy.taskapi.entity;

import com.nhnacademy.taskapi.dto.request.task.CreateTaskRequest;
import com.nhnacademy.taskapi.dto.request.task.ModifyTaskRequest;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Tasks")
@Getter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToOne
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "registrant_name", nullable = false)
    private String registrantName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Task(Project project, Milestone milestone, CreateTaskRequest request) {
        this.project = project;
        this.milestone = milestone;
        this.title = request.getTitle();
        this.content = request.getContent();
        this.registrantName = request.getRegistrantName();
        this.createdAt = LocalDateTime.now();
    }

    public void modifyTask(Milestone milestone, ModifyTaskRequest request) {
        this.milestone = milestone;
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
