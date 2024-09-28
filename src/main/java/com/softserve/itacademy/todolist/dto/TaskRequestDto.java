package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class TaskRequestDto {

    @JsonProperty("task_name")
    private String name;

    @JsonProperty("priority")
    private String priority;

}