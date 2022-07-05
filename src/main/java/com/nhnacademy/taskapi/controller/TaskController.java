package com.nhnacademy.taskapi.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import com.nhnacademy.taskapi.dto.request.task.CreateTaskRequest;
import com.nhnacademy.taskapi.dto.request.task.ModifyTaskRequest;
import com.nhnacademy.taskapi.dto.response.task.TaskListResponse;
import com.nhnacademy.taskapi.dto.response.task.TaskResponse;
import com.nhnacademy.taskapi.service.TaskService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Void> createTask(
        @Valid @RequestBody CreateTaskRequest createRequest) {

        taskService.createTask(createRequest);

        return ResponseEntity.status(CREATED)
                             .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findTask(@PathVariable Long id) {

        TaskResponse task = taskService.findTask(id);

        return ResponseEntity.status(OK)
                             .contentType(APPLICATION_JSON)
                             .body(task);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskListResponse>> findTaskList(@PathVariable Long projectId) {

        List<TaskListResponse> tasks = taskService.findTaskByProjectId(projectId);

        return ResponseEntity.status(OK)
                             .contentType(APPLICATION_JSON)
                             .body(tasks);
    }

    @PutMapping
    public ResponseEntity<Void> modifyTask(
        @Valid @RequestBody ModifyTaskRequest modifyRequest) {

        taskService.modifyTask(modifyRequest);

        return ResponseEntity.status(OK)
                             .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.status(NO_CONTENT)
                             .build();
    }


}
