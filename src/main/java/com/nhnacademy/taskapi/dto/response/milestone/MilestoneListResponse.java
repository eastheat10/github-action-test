package com.nhnacademy.taskapi.dto.response.milestone;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MilestoneListResponse {

    private final List<MilestoneResponse> milestoneResponseList;
}
