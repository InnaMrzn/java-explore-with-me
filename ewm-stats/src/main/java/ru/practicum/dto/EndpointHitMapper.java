package ru.practicum.dto;

import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    private static final DateTimeFormatter dateFormat
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toModel(EndpointHitDto dto) {
        return new EndpointHit(dto.getApp(), dto.getUri(), dto.getIp(),
                LocalDateTime.parse(dto.getTimestamp(), dateFormat));
    }

    public static ViewStatsDto toView(EndpointHit model) {
        return new ViewStatsDto(model.getApp(), model.getUri(),
                model.getHits());
    }

}
