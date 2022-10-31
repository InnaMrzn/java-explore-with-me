package ru.practicum.dto.mappers;

import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;

import java.time.format.DateTimeFormatter;

public class RequestMapper {
    private static final DateTimeFormatter dateFormat
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequest toRequest(Long userId, Long eventId) {
        return ParticipationRequest.builder()
                .requester(userId)
                .event(eventId)
                .build();
    }

    public static ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .requester(request.getRequester())
                .id(request.getId())
                .status(request.getStatus().name())
                .event(request.getEvent())
                .created(dateFormat.format(request.getCreated()))
                .build();
    }
}
