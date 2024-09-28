package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;

    @PostMapping("/{todo_id}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@PathVariable long todo_id, @RequestBody TaskRequestDto taskRequestDto) {
        log.info("[POST] Request to create task for ToDo ID: {}", todo_id);
        Task task = new Task();
        task.setName(taskRequestDto.getName());
        task.setPriority(Priority.valueOf(taskRequestDto.getPriority()));
        task.setTodo(todoService.readById(todo_id));
        task.setState(stateService.readById(1L));
        taskService.create(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{todo_id}/{task_id}")
                .buildAndExpand(task.getTodo().getId(),task.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TaskResponseDto(task));
    }

    @GetMapping("/{task_id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public TaskResponseDto read(@PathVariable long task_id) {
        log.info("[GET] Request to read task");
        return new TaskResponseDto(taskService.readById(task_id));
    }

    @DeleteMapping("/{task_id}/todos/{todo_id}/delete")
    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.id == #todo_id")
    public ResponseEntity<?> delete(@PathVariable long todo_id, @PathVariable long task_id) {
        log.info("[DELETE] Request to delete task");
        taskService.delete(task_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> getAll() {
        log.info("[GET] Request to read all tasks");
        return taskService.getAll().stream()
                .map(TaskResponseDto:: new)
                .collect(Collectors.toList());
    }
    @GetMapping("/todos/{todo_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> getAllTodoTask(@PathVariable long todo_id) {
        log.info("[GET] Request to read all tasks for current todo");
        return taskService.getByTodoId(todo_id).stream()
                .map(TaskResponseDto:: new)
                .collect(Collectors.toList());
    }

}