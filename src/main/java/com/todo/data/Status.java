package com.todo.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    DONE("done"),
    NOT_DONE("not done"),
    PAST_DUE("past due");

    private final String value;

}
