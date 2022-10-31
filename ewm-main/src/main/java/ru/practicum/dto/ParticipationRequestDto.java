package ru.practicum.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class ParticipationRequestDto {

    Long id;
    String created;
    @NotNull
    Long event;
    @NotNull
    Long requester;
    String status;

}