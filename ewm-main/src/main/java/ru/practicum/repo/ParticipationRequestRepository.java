package ru.practicum.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.RequestStatus;
import ru.practicum.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester(Long userId);

    Optional<ParticipationRequest> findByIdAndEvent(Long requestId, Long eventId);

    Optional<ParticipationRequest> findByIdAndRequester(Long requestId, Long userId);

    List<ParticipationRequest> findAllByEventAndStatus(Long eventId, RequestStatus status);

    @Query("select count(p.id) from ParticipationRequest p where p.event = :eventId")
    Long getCountApprovedForEvent(Long eventId);

    @Query(value = "select p.id, p.created_date, p.event_id, p.requester_id, p.request_status " +
            "from requests p" +
            " left join events e on p.event_id = e.id where p.event_id=:eventId " +
            "and e.initiator_id=:userId", nativeQuery = true)
    List<ParticipationRequest> searchByEventInitiator(Long eventId, Long userId);
}
