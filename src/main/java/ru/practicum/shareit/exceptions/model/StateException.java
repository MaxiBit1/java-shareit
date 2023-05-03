package ru.practicum.shareit.exceptions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StateException extends RuntimeException {
    private String text;
}
