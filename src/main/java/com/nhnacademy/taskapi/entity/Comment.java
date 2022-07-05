package com.nhnacademy.taskapi.entity;

import com.nhnacademy.taskapi.dto.request.comment.CreateCommentRequest;
import com.nhnacademy.taskapi.dto.request.comment.ModifyCommentRequest;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
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
@Table(name = "Comments")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    public Comment(Task task, CreateCommentRequest request) {
        this.task = task;
        this.username = request.getUsername();
        this.comment = request.getComment();
        this.createdAt = LocalDateTime.now();
    }

    public void modifyComment(ModifyCommentRequest request) {
        this.comment = request.getComment();
        this.modifiedAt = LocalDateTime.now();
    }
}
