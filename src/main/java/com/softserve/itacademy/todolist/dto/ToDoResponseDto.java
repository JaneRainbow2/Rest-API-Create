package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class ToDoResponseDto {
    Long id;
    String title;
    @JsonProperty("created_at")
    LocalDateTime createdAt;
    @JsonProperty("owner_id")
    long ownerId;
    List<Long> collaborators;


    public ToDoResponseDto(ToDo toDo) {
        id = toDo.getId();
        title = toDo.getTitle();
        createdAt = toDo.getCreatedAt();
        ownerId = toDo.getOwner().getId();
        collaborators = toDo.getCollaborators() != null ?
                toDo.getCollaborators().stream().map(User::getId).collect(Collectors.toList()) :
                new ArrayList<Long>();
    }
}