package com.todo.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ItemDto {

    @JsonProperty
    private String description;

    @JsonProperty(required = true)
    @NotNull(message = "Status is required")
    private Status status;

    private LocalDateTime creationDate;

    @JsonProperty(required = true)
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDateTime dueDate;

    private LocalDateTime markDate;
}
