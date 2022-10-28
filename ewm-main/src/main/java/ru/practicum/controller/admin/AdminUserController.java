package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                                  @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("GET /users");

        return ResponseEntity.status(HttpStatus.OK).body(userService.findUsers(ids, from, size));
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid NewUserRequest user) {
        log.info("POST /users");
        return ResponseEntity.status(HttpStatus.OK).body(userService.create(user));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable("userId") Long userId) {
        log.info("DELETE /users/" + userId);
        userService.delete(userId);
    }
}

