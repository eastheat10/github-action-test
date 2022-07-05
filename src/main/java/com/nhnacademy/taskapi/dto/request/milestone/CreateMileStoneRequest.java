package com.nhnacademy.taskapi.dto.request.milestone;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMileStoneRequest {

    @NotNull
    private Long projectId;

    @NotBlank
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
}
