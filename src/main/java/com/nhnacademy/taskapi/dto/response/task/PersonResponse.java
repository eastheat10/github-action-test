package com.nhnacademy.taskapi.dto.response.task;

import com.nhnacademy.taskapi.dto.projection.person.PersonDto;
import lombok.Getter;

@Getter
public class PersonResponse {

    private final Long taskId;
    private final Long memberId;
    private final String username;

    public PersonResponse(PersonDto dto) {
        this.taskId = dto.getTaskId();
        this.memberId = dto.getMemberId();
        this.username = dto.getUsername();
    }
}
