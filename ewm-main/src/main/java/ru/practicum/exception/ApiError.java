package ru.practicum.exception;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {

    private final List<String> errors;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;

}
