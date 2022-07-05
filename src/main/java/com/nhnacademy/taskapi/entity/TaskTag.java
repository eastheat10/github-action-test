package com.nhnacademy.taskapi.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
@Table(name = "Task_Tags")
@Getter
@NoArgsConstructor
public class TaskTag {

    @EmbeddedId
    private Pk id;

    @MapsId("taskId")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "task_id")
    private Task task;

    @MapsId("tagId")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public TaskTag(Task task, Tag tag) {
        this.id = new Pk(task.getId(), tag.getId());
        this.task = task;
        this.tag = tag;
    }

    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {

        private Long taskId;
        private Long tagId;
    }
}
