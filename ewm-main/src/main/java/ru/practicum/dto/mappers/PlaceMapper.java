package ru.practicum.dto.mappers;

import ru.practicum.dto.NewPlaceDto;
import ru.practicum.dto.PlaceDto;
import ru.practicum.model.Place;

public class PlaceMapper {

    public static Place toPlace(NewPlaceDto dto) {
        Place place = new Place();
        place.setName(dto.getName());
        place.setLocation(dto.getLocation());
        return place;
    }

    public static PlaceDto toPlaceDto(Place place) {
        return new PlaceDto(place.getId(), place.getName(), place.getLocation());
    }
}
