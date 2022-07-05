package com.nhnacademy.taskapi.dto.request.task;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTaskRequest {

    @NotNull
    private Long projectId;

    private Long milestoneId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String registrantName;

    private List<Long> tags = new ArrayList<>();
    private List<Long> people = new ArrayList<>(); // 담당자
}
