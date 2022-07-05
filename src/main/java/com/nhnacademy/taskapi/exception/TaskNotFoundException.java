package com.nhnacademy.taskapi.exception;

public class TaskNotFoundException extends IllegalArgumentException {

    public TaskNotFoundException() {
        super("업무를 찾을 수 없습니다.");
    }
}
