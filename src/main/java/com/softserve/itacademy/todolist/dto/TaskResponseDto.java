package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.Task;
import lombok.Value;

@Value
public class TaskResponseDto {
    private long id;

    private String name;

    private String priority;

    private long todoId;

    private long stateId;

    public TaskResponseDto(Task task){
        this.id= task.getId();
        this.name= task.getName();
        this.priority=task.getPriority().toString();
        this.stateId=task.getState().getId();
        this.todoId=task.getTodo().getId();
    }
}