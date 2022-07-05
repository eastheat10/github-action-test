package com.nhnacademy.taskapi.dto.request.task;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyTaskRequest {

    @NotNull
    private Long taskId;

    private Long milestoneId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<Long> tags;
    private List<Long> people; // 담당자
}
