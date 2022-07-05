package com.nhnacademy.taskapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.taskapi.dto.request.comment.CreateCommentRequest;
import com.nhnacademy.taskapi.dto.request.comment.ModifyCommentRequest;
import com.nhnacademy.taskapi.entity.Comment;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.repository.CommentRepository;
import com.nhnacademy.taskapi.repository.TaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    @DisplayName("댓글 추가")
    void addComment() {

        CreateCommentRequest request = spy(new CreateCommentRequest());

        when(request.getTaskId()).thenReturn(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mock(Task.class)));
        when(commentRepository.save(any(Comment.class))).thenReturn(mock(Comment.class));

        commentService.addComment(request);

        verify(taskRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정")
    void testModifyComment() {

        ModifyCommentRequest request = new ModifyCommentRequest();
        Comment spyComment = spy(new Comment());

        ReflectionTestUtils.setField(request, "id", 1L);
        ReflectionTestUtils.setField(request, "comment", "modify comment");

        when(commentRepository.findById(request.getId())).thenReturn(Optional.of(spyComment));
        doNothing().when(spyComment).modifyComment(request);

        commentService.modifyComment(request);

        verify(commentRepository, times(1)).findById(request.getId());
        verify(spyComment, times(1)).modifyComment(request);
    }

    @Test
    @DisplayName("댓글 삭제")
    void testDeleteComment() {

        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mock(Comment.class)));

        doNothing().when(commentRepository).delete(any(Comment.class));

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }
}