package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repo.EndpointHitRepository;

@Service
@Transactional
public class HitService {
    private final EndpointHitRepository hitRepository;

    public HitService(EndpointHitRepository repo) {
        this.hitRepository = repo;
    }

    public void saveEndpointHit(EndpointHitDto dto) {
        EndpointHit hit = EndpointHitMapper.toModel(dto);
        hitRepository.save(hit);
    }
}
