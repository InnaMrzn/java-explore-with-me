package ru.practicum.model;

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
    private Float radius;
}
