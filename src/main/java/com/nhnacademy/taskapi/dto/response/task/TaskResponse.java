package com.nhnacademy.taskapi.dto.response.task;

import com.nhnacademy.taskapi.dto.response.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.response.tag.TagResponse;
import com.nhnacademy.taskapi.entity.Task;
import java.util.List;
import lombok.Getter;

@Getter
public class TaskResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String registrantName;
    private final MilestoneResponse milestone;
    private final List<TagResponse> tags;
    private final List<PersonResponse> persons;
    private final List<CommentResponse> comments;

    public TaskResponse(Task task, MilestoneResponse milestone, List<TagResponse> tags,
                        List<PersonResponse> people, List<CommentResponse> comments) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.registrantName = task.getRegistrantName();
        this.milestone = milestone;
        this.tags = tags;
        this.persons = people;
        this.comments = comments;
    }
}
