package com.nhnacademy.taskapi.entity;

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
@Table(name = "The_person_in_charges")
@Getter
@NoArgsConstructor
public class ThePersonInCharge {

    @EmbeddedId
    private Pk id;

    @MapsId("taskId")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    public ThePersonInCharge(Task task, Long memberId) {
        this.id = new Pk(memberId, task.getId());
        this.task = task;
    }

    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {

        private Long taskId;

        @Column(name = "member_id", nullable = false)
        private Long memberId;
    }
}
