package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EndpointHitDto {

    private String app;

    private String uri;

    private String ip;

    private String timestamp;

}