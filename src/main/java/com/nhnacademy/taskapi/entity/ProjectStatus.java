package com.nhnacademy.taskapi.entity;

import lombok.Getter;

@Getter
public enum ProjectStatus {

    PROGRESS("활성"),
    DORMANT("휴면"),
    END("종료");

    ProjectStatus(String status) {
        this.status = status;
    }

    private final String status;
}
