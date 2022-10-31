package ru.practicum.model;

import lombok.Data;

@Data
public class ViewStats {
    private final String app;
    private final String uri;
    private final Long hits;
}
