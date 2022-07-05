package com.nhnacademy.taskapi.exception;

public class CommentNotFoundException extends IllegalArgumentException {
    public CommentNotFoundException() {
        super("댓글을 찾을 수 없습니다.");
    }
}
