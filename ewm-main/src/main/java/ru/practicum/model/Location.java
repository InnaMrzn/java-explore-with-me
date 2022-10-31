package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@RequiredArgsConstructor
@Getter
@Setter
public class Location {
    private Float lat;
    private Float lon;
    @JsonIgnore
    private Long radius;
}
