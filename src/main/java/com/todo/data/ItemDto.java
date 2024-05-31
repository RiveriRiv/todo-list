package com.todo.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@Builder
public class ItemDto {

    @JsonProperty
    private String description;

    @JsonProperty(required = true)
    @NotNull(message = "Status is required")
    private Status status;

    private Date creationDate;

    @JsonProperty(required = true)
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private Date dueDate;

    private Date markDate;
}
