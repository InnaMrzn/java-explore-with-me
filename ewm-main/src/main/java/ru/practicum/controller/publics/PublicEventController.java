package ru.practicum.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.controller.validation.RestrictedStringValues;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping(value = "/{id}")
    ResponseEntity<EventFullDto> getEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("GET /event/" + id);
        EventFullDto resultDto = eventService.findByIdForPublic(id);
        statsClient.save(request);

        return ResponseEntity.status(HttpStatus.OK).body(resultDto);

    }

    @GetMapping
    ResponseEntity<List<EventShortDto>> getEvents(@Valid @RequestParam(value = "text", required = false) String text,
                                                  @RequestParam(value = "categories", required = false) List<Long> categories,
                                                  @RequestParam(value = "paid", required = false) String paid,
                                                  @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                  @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                  @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") String onlyAvailable,
                                                  @RestrictedStringValues(propName = "sort", values = {"EVENT_DATE", "VIEWS"})
                                                  @RequestParam(value = "sort", required = false) String sort,
                                                  @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size, HttpServletRequest request) {

        List<EventShortDto> resultDtos = eventService.findFilteredEventsForPublic(text,
                categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        statsClient.save(request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDtos);
    }

}
