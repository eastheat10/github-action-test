package com.nhnacademy.taskapi.dto.request.milestone;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyMileStoneRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;
}
