package com.nhnacademy.taskapi.dto.response.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.taskapi.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final String username;
    private final String comment;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm")
    private final LocalDateTime modifiedAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUsername();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}