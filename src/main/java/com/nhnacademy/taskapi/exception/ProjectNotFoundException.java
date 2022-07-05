package com.nhnacademy.taskapi.exception;

public class ProjectNotFoundException extends IllegalArgumentException {
    public ProjectNotFoundException() {
        super("해당 프로젝트를 찾을 수 없습니다.");
    }
}
