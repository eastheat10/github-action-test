package com.nhnacademy.taskapi.dto.request.project;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddProjectMemberRequest {

    @NotNull
    private Long projectId;

    @NotEmpty
    private List<MemberInfo> memberInfoList = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    public static class MemberInfo {
        private Long memberId;
        private String username;
    }
}
