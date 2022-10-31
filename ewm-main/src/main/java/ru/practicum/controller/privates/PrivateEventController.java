package ru.practicum.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable("userId") Long userId,
                                                 @Valid @NotNull @RequestBody NewEventDto eventDto) {
        log.info("POST /events");
        return ResponseEntity.status(HttpStatus.OK).body(eventService.create(userId, eventDto));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEvent(@PathVariable("userId") Long userId,
                                                    @PathVariable("eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.cancelByUser(userId, eventId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable("userId") Long userId,
                                                 @PathVariable("eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findByIdAndUser(userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(@PathVariable("userId") Long userId,
                                                                              @PathVariable("eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findEventRequestsForUser(userId, eventId));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@PathVariable("userId") Long userId,
                                                         @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("GET /events");
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findByInitiator(userId, from, size));
    }

    @PatchMapping
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable("userId") Long userId,
                                                    @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("UPDATE /events" + " user id=" + userId + " event id=" + eventDto.getEventId());
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateByUser(eventDto));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<ParticipationRequestDto> rejectParticipationRequest(@PathVariable("userId") Long userId,
                                                                              @PathVariable("eventId") Long eventId,
                                                                              @PathVariable("reqId") Long reqId) {
        log.info("PATCH /users/" + userId + "/events/" + eventId + "/requests/" + reqId + "/reject");
        return ResponseEntity.status(HttpStatus.OK).body(eventService.rejectRequest(userId, eventId, reqId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<ParticipationRequestDto> confirmParticipationRequest(@PathVariable("userId") Long userId,
                                                                               @PathVariable("eventId") Long eventId,
                                                                               @PathVariable("reqId") Long reqId) {
        log.info("PATCH /users/" + userId + "/events/" + eventId + "/requests/" + reqId + "/confirm");
        return ResponseEntity.status(HttpStatus.OK).body(eventService.confirmRequest(userId, eventId, reqId));
    }


}
