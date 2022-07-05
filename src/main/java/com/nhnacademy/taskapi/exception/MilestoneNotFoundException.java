package com.nhnacademy.taskapi.exception;

public class MilestoneNotFoundException extends IllegalArgumentException {
    public MilestoneNotFoundException() {
        super("마일스톤을 찾을 수 없습니다.");
    }
}
