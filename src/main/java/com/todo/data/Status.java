package com.todo.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Status {
    DONE("DONE"),
    NOT_DONE("NOT_DONE"),
    PAST_DUE("PAST_DUE");

    private final String value;

    Status(String value) {
        this.value = value.toUpperCase();
    }

    @JsonCreator
    public static Status fromString(String text) {
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Not valid status");
    }

}
