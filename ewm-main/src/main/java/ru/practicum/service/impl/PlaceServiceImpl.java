package ru.practicum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.NewPlaceDto;
import ru.practicum.dto.PlaceDto;
import ru.practicum.dto.mappers.PlaceMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Place;
import ru.practicum.repo.PlaceRepository;
import ru.practicum.service.PlaceService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;

    }

    @Override
    public List<PlaceDto> findAllPlaces(int from, int size) {
        int page = from / size;
        Pageable paging = PageRequest.of(page, size);
        List<Place> places = placeRepository.findAll(paging).getContent();
        return places.stream().map(PlaceMapper::toPlaceDto).collect(Collectors.toList());
    }

    @Override
    public PlaceDto findPlace(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new NotFoundException("Место с Id=" + placeId + " не найдено."));
        return PlaceMapper.toPlaceDto(place);
    }

    @Override
    @Transactional
    public PlaceDto create(NewPlaceDto placeDto) {
        Place place = PlaceMapper.toPlace(placeDto);
        return PlaceMapper.toPlaceDto(placeRepository.save(place));
    }

}
