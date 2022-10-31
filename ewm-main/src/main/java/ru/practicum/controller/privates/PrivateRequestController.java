package ru.practicum.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.ParticipationRequestService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateRequestController {

    private final ParticipationRequestService requestService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(@PathVariable("userId") Long userId,
                                                                           @NotNull @RequestParam(value = "eventId") Long eventId) {
        log.info("POST /requests" + " user id=" + userId + " event id=" + eventId);
        return ResponseEntity.status(HttpStatus.OK).body(requestService.create(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable("userId") Long userId,
                                                                 @PathVariable("requestId") Long requestId) {
        log.info("UPDATE /requests" + " user id=" + userId + " event id=" + requestId);
        return ResponseEntity.status(HttpStatus.OK).body(requestService.cancel(userId, requestId));
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(@PathVariable("userId") Long userId) {
        log.info("GET /events");
        return ResponseEntity.status(HttpStatus.OK).body(requestService.findUserRequests(userId));
    }


}
