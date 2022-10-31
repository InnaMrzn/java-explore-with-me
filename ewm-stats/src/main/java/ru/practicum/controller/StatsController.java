package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<List<ViewStatsDto>> getStats(
            @RequestParam(name = "start", required = false, defaultValue = "2000-01-01 10:00:00") String start,
            @RequestParam(name = "end", required = false, defaultValue = "2030-01-01 10:00:00") String end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", required = false, defaultValue = "false") String unique)
            throws UnsupportedEncodingException {
        log.info("GET /stats with uri: ");
        String decodedStart = URLDecoder.decode(start, StandardCharsets.UTF_8.toString());
        String decodedEnd = URLDecoder.decode(end, StandardCharsets.UTF_8.toString());
        List<String> decodedUris = null;
        if (uris != null) {
            decodedUris = new ArrayList<>();
            for (String nextUri : uris) {
                decodedUris.add(URLDecoder.decode(nextUri, StandardCharsets.UTF_8.toString()));
            }
        }

        String decodedUnique = URLDecoder.decode(unique, StandardCharsets.UTF_8.toString());

        return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(decodedStart, decodedEnd, decodedUris,
                decodedUnique));

    }

}
