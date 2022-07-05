package com.nhnacademy.taskapi.service;

import static java.util.stream.Collectors.toList;

import com.nhnacademy.taskapi.dto.request.task.CreateTaskRequest;
import com.nhnacademy.taskapi.dto.request.task.ModifyTaskRequest;
import com.nhnacademy.taskapi.dto.response.milestone.MilestoneResponse;
import com.nhnacademy.taskapi.dto.response.tag.TagResponse;
import com.nhnacademy.taskapi.dto.response.task.CommentResponse;
import com.nhnacademy.taskapi.dto.response.task.PersonResponse;
import com.nhnacademy.taskapi.dto.response.task.TaskListResponse;
import com.nhnacademy.taskapi.dto.response.task.TaskResponse;
import com.nhnacademy.taskapi.entity.Milestone;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.entity.TaskTag;
import com.nhnacademy.taskapi.entity.ThePersonInCharge;
import com.nhnacademy.taskapi.exception.ProjectNotFoundException;
import com.nhnacademy.taskapi.exception.TaskNotFoundException;
import com.nhnacademy.taskapi.repository.CommentRepository;
import com.nhnacademy.taskapi.repository.MilestoneRepository;
import com.nhnacademy.taskapi.repository.PersonRepository;
import com.nhnacademy.taskapi.repository.ProjectMembersRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import com.nhnacademy.taskapi.repository.TagRepository;
import com.nhnacademy.taskapi.repository.TaskRepository;
import com.nhnacademy.taskapi.repository.TaskTagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MilestoneRepository milestoneRepository;
    private final TagRepository tagRepository;
    private final TaskTagRepository taskTagRepository;
    private final ProjectMembersRepository projectMembersRepository;
    private final PersonRepository personRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createTask(CreateTaskRequest createRequest) {

        Project project = projectRepository.findById(createRequest.getProjectId())
                                           .orElseThrow(ProjectNotFoundException::new);

        Milestone milestone =
            milestoneRepository.findById(Optional.ofNullable(createRequest.getMilestoneId())
                                                 .orElse(0L))
                               .orElse(null);

        Task task = new Task(project, milestone, createRequest);

        Task savedTask = taskRepository.save(task);

        // 업무가 생성될 때 함께 요청된 태그 목록
        List<TaskTag> taskTagList = Optional.ofNullable(createRequest.getTags())
                                            .orElse(new ArrayList<>())
                                            .stream()
                                            .map(t -> tagRepository.findById(t).orElse(null))
                                            .filter(Objects::nonNull)
                                            .map(tag -> new TaskTag(savedTask, tag))
                                            .collect(toList());

        // 담당자
        List<ThePersonInCharge> persons = Optional.ofNullable(createRequest.getPeople())
                                                  .orElse(new ArrayList<>())
                                                  .stream()
                                                  .filter(
                                                      projectMembersRepository::existsById_memberId)
                                                  .map(person ->
                                                      new ThePersonInCharge(savedTask, person))
                                                  .collect(toList());

        if (taskTagList.size() > 0) {
            taskTagRepository.saveAll(taskTagList);
        }

        if (persons.size() > 0) {
            personRepository.saveAll(persons);
        }
    }

    public TaskResponse findTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                                  .orElseThrow(TaskNotFoundException::new);

        Milestone milestone = task.getMilestone();
        MilestoneResponse milestoneResponse = null;

        if (Objects.nonNull(milestone)) {
            milestoneResponse = new MilestoneResponse(milestone);
        }

        List<TagResponse> tags =
            taskTagRepository.findByTaskId(task.getId())
                             .stream()
                             .map(taskTag -> tagRepository.findById(taskTag.getTag().getId())
                                                          .orElse(null))
                             .filter(Objects::nonNull)
                             .map(TagResponse::new)
                             .collect(toList());

        List<PersonResponse> people = personRepository.findDtoByTaskId(task.getId())
                                                      .stream()
                                                      .map(PersonResponse::new)
                                                      .collect(toList());

        List<CommentResponse> comments = commentRepository.findByTaskId(task.getId())
                                                          .stream()
                                                          .map(CommentResponse::new)
                                                          .collect(toList());

        return new TaskResponse(task, milestoneResponse, tags, people, comments);
    }

    public List<TaskListResponse> findTaskByProjectId(Long projectId) {

        return taskRepository.findByProject_Id(projectId)
                             .stream()
                             .map(TaskListResponse::new)
                             .collect(toList());
    }

    @Transactional
    public void modifyTask(ModifyTaskRequest modifyTaskRequest) {

        Task task = taskRepository.findById(modifyTaskRequest.getTaskId())
                                  .orElseThrow(TaskNotFoundException::new);

        Milestone milestone =
            milestoneRepository.findById(Optional.ofNullable(modifyTaskRequest.getMilestoneId())
                                                 .orElse(0L))
                               .orElse(null);

        task.modifyTask(milestone, modifyTaskRequest);
        taskRepository.flush();

        // 태그 삭제 후 재생성
        List<TaskTag> taskTags = taskTagRepository.findByTaskId(task.getId());
        taskTagRepository.deleteAll(taskTags);

        List<TaskTag> taskTagList = Optional.ofNullable(modifyTaskRequest.getTags())
                                            .orElse(new ArrayList<>())
                                            .stream()
                                            .map(t -> tagRepository.findById(t)
                                                                   .orElse(null))
                                            .filter(Objects::nonNull)
                                            .map(tag -> new TaskTag(task, tag))
                                            .collect(toList());

        taskTagRepository.saveAll(taskTagList);

        // 담당자 삭제 후 재생성
        List<ThePersonInCharge> people = personRepository.findByTaskId(task.getId());
        personRepository.deleteAll(people);

        // 프로젝트 멤버중 선택된 담당자
        List<ThePersonInCharge> selectedPeople = Optional.ofNullable(modifyTaskRequest.getPeople())
                                                         .orElse(new ArrayList<>())
                                                         .stream()
                                                         .filter(
                                                             projectMembersRepository::existsById_memberId)
                                                         .map(person ->
                                                             new ThePersonInCharge(task,
                                                                 person))
                                                         .collect(toList());

        personRepository.saveAll(selectedPeople);
    }

    @Transactional
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                                  .orElseThrow(TaskNotFoundException::new);

        List<TaskTag> taskTags = taskTagRepository.findByTaskId(id);
        taskTagRepository.deleteAll(taskTags);

        taskRepository.delete(task);
    }
}
