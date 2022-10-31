package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.AdminUpdateEventRequest;
import ru.practicum.dto.EventFullDto;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = "states", required = false) List<String> states,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        return ResponseEntity.status(HttpStatus.OK).body(eventService.findFilteredEventsForAdmin(users,
                states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.publish(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.reject(eventId));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable("eventId") Long eventId,
                                                    @Valid @RequestBody AdminUpdateEventRequest body) {
        log.info("UPDATE /admin/events event id=" + eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateByAdmin(eventId, body));
    }

}
