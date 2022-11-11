package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewPlaceDto;
import ru.practicum.dto.PlaceDto;
import ru.practicum.service.PlaceService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/places")
@RequiredArgsConstructor
@Slf4j
@Validated

public class AdminPlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<PlaceDto> addPlace(@Valid @NotNull @RequestBody NewPlaceDto body) {
        log.info("POST /places");
        return ResponseEntity.status(HttpStatus.OK).body(placeService.create(body));
    }

}
