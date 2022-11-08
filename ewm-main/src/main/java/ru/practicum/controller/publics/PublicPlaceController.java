package ru.practicum.controller.publics;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.PlaceDto;
import ru.practicum.service.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicPlaceController {
    private final PlaceService placeService;

    @GetMapping
    ResponseEntity<List<PlaceDto>> getPlaces(
            @RequestParam(value = "from", required = false,
                    defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        log.info("GET /places?from="+from+"&size="+size);
        return ResponseEntity.status(HttpStatus.OK).body(placeService.findAllPlaces(from, size));
    }

    @GetMapping("/{placeId}")
    ResponseEntity<PlaceDto> getPlace(@PathVariable("placeId") Long placeId) {
        log.info("GET /places/" + placeId);
        return ResponseEntity.status(HttpStatus.OK).body(placeService.findPlace(placeId));
    }


}
