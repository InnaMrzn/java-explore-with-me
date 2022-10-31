package ru.practicum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EventState;
import ru.practicum.RequestStatus;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.mappers.RequestMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongActionException;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.repo.EventRepository;
import ru.practicum.repo.ParticipationRequestRepository;
import ru.practicum.service.ParticipationRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ParticipationRequestServiceImpl(ParticipationRequestRepository requestRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId +
                        " не найдено."));
        if (event.getInitiator().getId().longValue() == userId.longValue()) {
            throw new WrongActionException("нельзя подать заявку на собственное событие.");
        }
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new WrongActionException("события с id=" + eventId + " нет в списке опубликованных");
        }
        if (event.getApprovedRequestsCount() != 0
                && event.getApprovedRequestsCount() >= event.getParticipantLimit()) {
            throw new WrongActionException("для события с id=" + eventId + " достигнут лимит участия. Заявка отклонена");
        }
        ParticipationRequest request = RequestMapper.toRequest(userId, eventId);
        request.setCreated(LocalDateTime.now());
        if (!event.isRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequester(requestId, userId).orElseThrow(() ->
                new NotFoundException("Запрос с id= " + requestId + " не найден для пользователя id=" + userId));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findUserRequests(Long userId) {
        return requestRepository.findAllByRequester(userId)
                .stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }
}
