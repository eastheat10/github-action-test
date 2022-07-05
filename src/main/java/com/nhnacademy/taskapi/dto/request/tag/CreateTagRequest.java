package com.nhnacademy.taskapi.dto.request.tag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTagRequest {

    @NotNull
    private Long projectId;

    @NotBlank
    private String name;
}
