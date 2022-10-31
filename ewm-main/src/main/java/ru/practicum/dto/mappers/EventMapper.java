package ru.practicum.dto.mappers;

import ru.practicum.dto.*;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    private static final DateTimeFormatter dateFormat
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event updateEvent(UpdateEventRequest updateEvent, Event event) {
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), dateFormat));
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        return event;
    }

    public static Event updateEventForAdmin(AdminUpdateEventRequest updateEvent, Event event) {
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), dateFormat));
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        return event;
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .paid(Boolean.toString(event.isPaid()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .eventDate(dateFormat.format(event.getEventDate()))
                .confirmedRequests(event.getApprovedRequestsCount())
                .title(event.getTitle())
                .confirmedRequests(event.getApprovedRequestsCount()).build();

    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto fullDto = EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .paid(Boolean.toString(event.isPaid()))
                .title(event.getTitle())
                .state(event.getState().name())
                .requestModeration(Boolean.toString(event.isRequestModeration()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(dateFormat.format(event.getEventDate()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .location(event.getLocation())
                .createdOn(dateFormat.format(event.getCreatedOn()))
                .confirmedRequests(event.getApprovedRequestsCount()).build();
        if (event.getPublishedOn() != null) {
            fullDto.setPublishedOn(dateFormat.format(event.getPublishedOn()));
        }
        return fullDto;
    }

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .paid(newEventDto.isPaid())
                .description(newEventDto.getDescription())
                .requestModeration(newEventDto.isRequestModeration())
                .participantLimit(newEventDto.getParticipantLimit())
                .location(newEventDto.getLocation()).build();
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), dateFormat));

        return event;
    }
}
