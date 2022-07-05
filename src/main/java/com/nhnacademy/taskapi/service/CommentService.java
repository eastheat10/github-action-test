package com.nhnacademy.taskapi.service;

import com.nhnacademy.taskapi.dto.request.comment.CreateCommentRequest;
import com.nhnacademy.taskapi.dto.request.comment.ModifyCommentRequest;
import com.nhnacademy.taskapi.entity.Comment;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.exception.CommentNotFoundException;
import com.nhnacademy.taskapi.exception.TaskNotFoundException;
import com.nhnacademy.taskapi.repository.CommentRepository;
import com.nhnacademy.taskapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void addComment(CreateCommentRequest commentRequest) {

        Task task = taskRepository.findById(commentRequest.getTaskId())
                                  .orElseThrow(TaskNotFoundException::new);

        commentRepository.save(new Comment(task, commentRequest));
    }

    @Transactional
    public void modifyComment(ModifyCommentRequest commentRequest) {

        Comment comment = commentRepository.findById(commentRequest.getId())
                                           .orElseThrow(CommentNotFoundException::new);

        comment.modifyComment(commentRequest);
    }

    @Transactional
    public void deleteComment(Long id) {

        Comment comment = commentRepository.findById(id)
                                           .orElseThrow(CommentNotFoundException::new);

        commentRepository.delete(comment);
    }
}
