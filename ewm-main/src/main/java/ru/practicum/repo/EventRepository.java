package ru.practicum.repo;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiator_Id(Long eventId, Long userId);

    Long countByCategory_Id(Long catId);


}
