package ru.practicum.service;

import ru.practicum.dto.NewPlaceDto;
import ru.practicum.dto.PlaceDto;

import java.util.List;

public interface PlaceService {

    PlaceDto create(NewPlaceDto place);

    List<PlaceDto> findAllPlaces(int from, int size);

    PlaceDto findPlace(Long placeId);
}
