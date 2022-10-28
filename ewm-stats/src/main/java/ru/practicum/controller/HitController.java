package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.service.HitService;

import javax.validation.Valid;


@RestController
@RequestMapping("/hit")
@RequiredArgsConstructor
@Slf4j
@Validated
public class HitController {
    private final HitService hitService;

    @PostMapping
    public void hit(@Valid @RequestBody EndpointHitDto body) {
        log.info("POST /hit");
        hitService.saveEndpointHit(body);
    }

}
