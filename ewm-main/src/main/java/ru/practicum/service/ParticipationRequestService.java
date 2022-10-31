package ru.practicum.service;

import ru.practicum.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> findUserRequests(Long userId);
}
