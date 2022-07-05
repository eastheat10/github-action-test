package com.nhnacademy.taskapi.controller;

import static org.springframework.http.HttpStatus.*;

import com.nhnacademy.taskapi.dto.request.comment.CreateCommentRequest;
import com.nhnacademy.taskapi.dto.request.comment.ModifyCommentRequest;
import com.nhnacademy.taskapi.service.CommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> addComment(@Valid @RequestBody CreateCommentRequest commentRequest) {

        commentService.addComment(commentRequest);

        return ResponseEntity.status(CREATED)
                             .build();
    }

    @PutMapping
    public ResponseEntity<Void> modifyComment(@Valid @RequestBody ModifyCommentRequest modifyRequest) {

        commentService.modifyComment(modifyRequest);

        return ResponseEntity.status(OK)
                             .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {

        commentService.deleteComment(id);

        return ResponseEntity.status(NO_CONTENT)
                             .build();
    }
}
