package ru.practicum.dto;

import lombok.Data;

@Data
public class ViewStatsDto {
    private final String app;
    private final String uri;
    private final Long hits;
}
