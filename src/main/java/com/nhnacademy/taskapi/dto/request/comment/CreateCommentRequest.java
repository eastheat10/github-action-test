package com.nhnacademy.taskapi.dto.request.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentRequest {

    @NotNull
    private Long taskId;

    @NotBlank
    private String username;

    @NotBlank
    private String comment;
}
