package ru.practicum.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.EndpointHit;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
}
