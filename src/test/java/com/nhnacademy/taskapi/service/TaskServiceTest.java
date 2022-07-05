package com.nhnacademy.taskapi.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.taskapi.dto.request.task.CreateTaskRequest;
import com.nhnacademy.taskapi.dto.request.task.ModifyTaskRequest;
import com.nhnacademy.taskapi.dto.response.task.TaskListResponse;
import com.nhnacademy.taskapi.dto.response.task.TaskResponse;
import com.nhnacademy.taskapi.entity.Project;
import com.nhnacademy.taskapi.entity.ProjectStatus;
import com.nhnacademy.taskapi.entity.Task;
import com.nhnacademy.taskapi.repository.CommentRepository;
import com.nhnacademy.taskapi.repository.MilestoneRepository;
import com.nhnacademy.taskapi.repository.PersonRepository;
import com.nhnacademy.taskapi.repository.ProjectMembersRepository;
import com.nhnacademy.taskapi.repository.ProjectRepository;
import com.nhnacademy.taskapi.repository.TagRepository;
import com.nhnacademy.taskapi.repository.TaskRepository;
import com.nhnacademy.taskapi.repository.TaskTagRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private MilestoneRepository milestoneRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TaskTagRepository taskTagRepository;
    @Mock
    private ProjectMembersRepository projectMembersRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private CommentRepository commentRepository;


    @Test
    @DisplayName("업무 생성")
    void testCreateTask() {

        CreateTaskRequest request = new CreateTaskRequest();

        ReflectionTestUtils.setField(request, "projectId", 1L);
        ReflectionTestUtils.setField(request, "milestoneId", 0L);
        ReflectionTestUtils.setField(request, "tags", new ArrayList<>());
        ReflectionTestUtils.setField(request, "people", new ArrayList<>());

        Task task = mock(Task.class);

        when(projectRepository.findById(request.getProjectId())).thenReturn(
            Optional.of(mock(Project.class)));
        lenient().when(tagRepository.findById(anyLong())).thenReturn(null);
        lenient().when(projectMembersRepository.existsById_memberId(anyLong())).thenReturn(false);

        lenient().when(task.getId()).thenReturn(1L);

        taskService.createTask(request);
    }

    @Test
    @DisplayName("업무 조회")
    void findTask() {

        Long taskId = 1L;
        Task task = spy(new Task());

        when(task.getId()).thenReturn(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskTagRepository.findByTaskId(anyLong())).thenReturn(new ArrayList<>());
        when(personRepository.findDtoByTaskId(task.getId())).thenReturn(new ArrayList<>());
        when(commentRepository.findByTaskId(task.getId())).thenReturn(new ArrayList<>());

        TaskResponse response = taskService.findTask(taskId);

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("프로젝트에 해당하는 업무 목록 조회")
    void testFindTaskByProjectId() {

        Long projectId = 1L;

        Project project = new Project();

        ReflectionTestUtils.setField(project, "id", projectId);
        ReflectionTestUtils.setField(project, "adminId", 99L);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROGRESS);
        ReflectionTestUtils.setField(project, "name", "project name");
        ReflectionTestUtils.setField(project, "startDate", LocalDate.now());

        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = new Task();
            ReflectionTestUtils.setField(task, "project", project);
            ReflectionTestUtils.setField(task, "title", "title" + i);
            ReflectionTestUtils.setField(task, "content", "content");
            ReflectionTestUtils.setField(task, "registrantName", "registrantName");
            ReflectionTestUtils.setField(task, "createdAt", LocalDateTime.now());
            taskList.add(task);

        }

        when(taskRepository.findByProject_Id(projectId)).thenReturn(taskList);

        List<TaskListResponse> taskByProjectId = taskService.findTaskByProjectId(projectId);

        assertThat(taskByProjectId.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("업무 삭제")
    void testDeleteTask() {

        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mock(Task.class)));
        doNothing().when(taskRepository).delete(any(Task.class));

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    @DisplayName("업무 수정")
    void testModifyTask() {

        Long taskId = 1L;
        Task task = spy(new Task());
        ReflectionTestUtils.setField(task, "id", taskId);

        ModifyTaskRequest request = new ModifyTaskRequest();

        ReflectionTestUtils.setField(request, "taskId", taskId);
        ReflectionTestUtils.setField(request, "milestoneId", 0L);
        ReflectionTestUtils.setField(request, "title", "title1");
        ReflectionTestUtils.setField(request, "content", "content1");
        ReflectionTestUtils.setField(request, "tags", new ArrayList<>());
        ReflectionTestUtils.setField(request, "people", new ArrayList<>());

        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));

        doNothing().when(taskTagRepository).deleteAll(anyCollection());
        when(taskTagRepository.findByTaskId(task.getId())).thenReturn(new ArrayList<>());
        when(taskTagRepository.saveAll(anyCollection())).thenReturn(new ArrayList<>());

        when(personRepository.findByTaskId(task.getId())).thenReturn(new ArrayList<>());
        doNothing().when(personRepository).deleteAll(anyCollection());
        when(personRepository.saveAll(anyCollection())).thenReturn(new ArrayList<>());

        taskService.modifyTask(request);

        verify(task, times(1)).modifyTask(any(), any(ModifyTaskRequest.class));
    }
}