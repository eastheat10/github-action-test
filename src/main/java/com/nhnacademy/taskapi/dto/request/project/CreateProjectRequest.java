package com.nhnacademy.taskapi.dto.request.project;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProjectRequest {

    @NotNull
    private Long adminId;

    @NotNull
    private String adminUsername;

    @NotBlank
    private String projectName;

    private String content;
}
