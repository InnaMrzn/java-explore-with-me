package ru.practicum.service;

import ru.practicum.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto updateByUser(UpdateEventRequest eventDto);

    EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest body);

    EventFullDto cancelByUser(Long userId, Long eventId);

    EventFullDto findByIdAndUser(Long userId, Long eventId);

    EventFullDto findByIdForPublic(Long eventId);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);

    List<EventShortDto> findByInitiator(Long userId, Integer from, Integer size);

    ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long requestId);

    List<ParticipationRequestDto> findEventRequestsForUser(Long userId, Long eventId);

    List<EventFullDto> findFilteredEventsForAdmin(List<Long> users, List<String> states, List<Long> categories,
             String rangeStart, String rangeEnd, int from, int size);

    List<EventShortDto> findFilteredEventsForPublic(String text, List<Long> categories, String paid,
                                                    String rangeStart, String rangeEnd, String onlyAvailable,
                                                    String sort, int from, int size);
}

