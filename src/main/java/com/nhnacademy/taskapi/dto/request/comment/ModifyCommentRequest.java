package com.nhnacademy.taskapi.dto.request.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyCommentRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String comment;
}
