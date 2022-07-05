package com.nhnacademy.taskapi.entity;

import com.nhnacademy.taskapi.dto.request.project.AddProjectMemberRequest;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Project_Members")
@Getter
@NoArgsConstructor
public class ProjectMember {

    @EmbeddedId
    private Pk id;

    @MapsId("projectId")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "project_id")
    private Project project;

    private String username;

    public ProjectMember(Project project, AddProjectMemberRequest.MemberInfo info) {
        this.id = new Pk(project.getId(), info.getMemberId());
        this.project = project;
        this.username = info.getUsername();
    }

    public ProjectMember(Project project, Long memberId, String username) {
        this.id = new Pk(project.getId(), memberId);
        this.project = project;
        this.username = username;
    }

    @Getter
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Pk implements Serializable {

        private Long projectId;

        @Column(name = "member_id", nullable = false)
        private Long memberId;
    }
}
