package ru.practicum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EventState;
import ru.practicum.RequestStatus;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.*;
import ru.practicum.dto.mappers.EventMapper;
import ru.practicum.dto.mappers.RequestMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongActionException;
import ru.practicum.model.*;
import ru.practicum.repo.*;
import ru.practicum.service.EventService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;
    private final StatsClient statsClient;
    private static final DateTimeFormatter dateFormat
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ParticipationRequestRepository requestRepository,
                            UserRepository userRepository, CategoryRepository categoryRepository,
                            PlaceRepository placeRepository,
                            StatsClient statsClient) {
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.statsClient = statsClient;
        this.placeRepository = placeRepository;
    }

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        Event event = EventMapper.toEvent(eventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new WrongActionException("время нового события не может быть раньше, " +
                    "чем через два часа от текущего момента. " +
                    "Попытка создать события с датой начала " + eventDto.getEventDate());
        }
        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не найден."));
        event.setInitiator(initiator);
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                new NotFoundException("Категория с id=" + userId + " не найдена."));
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateByUser(UpdateEventRequest eventDto) {
        Event oldEvent = eventRepository.findById(eventDto.getEventId()).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventDto.getEventId() + " не найдено."));
        if (!(oldEvent.getState().equals(EventState.PENDING) || oldEvent.getState().equals(EventState.CANCELED))) {
            log.warn("пользователь может редактировать только события в статусе PENDING и CANCELED." +
                    "Статус текущего саобытия " + oldEvent.getState());
            return EventMapper.toEventFullDto(oldEvent);
        }
        if (oldEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongActionException("Можно редактировать событие не позже чем за 2 часа до его начала." +
                    "Событие с id=" + eventDto.getEventId() + " начнется " + dateFormat.format(oldEvent.getEventDate()));
        }
        Event event = EventMapper.updateEvent(eventDto, oldEvent);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongActionException("Новая дата должна быть не позже чем за 2 часа до его начала." +
                    "Событие с id=" + eventDto.getEventId() + " начнется " + dateFormat.format(event.getEventDate()));
        }

        if (eventDto.getCategory() != null && event.getCategory().getId().longValue() != (eventDto.getCategory())) {
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория с id=" + eventDto.getCategory() + " не найдена"));
            event.setCategory(category);
        }
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest body) {
        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено."));
        Event event = EventMapper.updateEventForAdmin(body, oldEvent);
        if (body.getCategory() != null && event.getCategory().getId().longValue() != (body.getCategory())) {
            Category category = categoryRepository.findById(body.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория с id=" + body.getCategory() + " не найдена"));
            event.setCategory(category);
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public List<EventFullDto> findFilteredEventsForAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                         String rangeStart, String rangeEnd, int from, int size) {
        Pageable sortedPaging = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
        Session session = entityManager.unwrap(Session.class);
        if (users != null) {
            Filter userFilter = session.enableFilter("userFilter");
            userFilter.setParameterList("users", users);
        }
        if (states != null) {
            Filter stateFilter = session.enableFilter("stateFilter");
            stateFilter.setParameterList("states", states);
        }
        if (categories != null) {
            Filter categoryFilter = session.enableFilter("categoryFilter");
            categoryFilter.setParameterList("categories", categories);
        }
        if (rangeStart != null && !rangeStart.equalsIgnoreCase("null")) {
            Filter rangeStartFilter = session.enableFilter("rangeStartFilter");
            rangeStartFilter.setParameter("rangeStart", LocalDateTime.parse(rangeStart, dateFormat));
        }
        if (rangeEnd != null && !rangeEnd.equalsIgnoreCase("null")) {
            Filter rangeEndFilter = session.enableFilter("rangeEndFilter");
            rangeEndFilter.setParameter("rangeEnd", LocalDateTime.parse(rangeEnd, dateFormat));
        }

        List<Event> events = eventRepository.findAll(sortedPaging).getContent();
        session.disableFilter("userFilter");
        session.disableFilter("categoryFilter");
        session.disableFilter("stateFilter");
        session.disableFilter("rangeStartFilter");
        session.disableFilter("rangeEndFilter");

        List<EventFullDto> eventDtos = events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
        Map<Long, Long> viewMap = getViews(new ArrayList<>(eventDtos));
        for (EventDto nextDto : eventDtos) {
            if ((viewMap.get(nextDto.getId()) != null)) {
                nextDto.setViews(viewMap.get(nextDto.getId()));
            }
        }

        return eventDtos;
    }

    @Override
    @Transactional
    public List<EventShortDto> findFilteredEventsForPublic(String text, List<Long> categories, Boolean paid,
                                                           String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                           String sort, Long placeId, int from, int size) {
        String sortProp = "eventDate";
        Pageable sortedPaging = PageRequest.of(from / size, size, Sort.by(sortProp).descending());
        boolean dateRangeExists = false;
        Session session = entityManager.unwrap(Session.class);
        Filter stateFilter = session.enableFilter("stateFilter");
        stateFilter.setParameter("states", EventState.PUBLISHED.name());
        if (text != null && !text.equalsIgnoreCase("null")) {
            Filter textFilter = session.enableFilter("textFilter");
            textFilter.setParameter("text", text);
        }
        if (categories != null) {
            Filter categoryFilter = session.enableFilter("categoryFilter");
            categoryFilter.setParameterList("categories", categories);
        }
        if (paid != null) {
            Filter paidFilter = session.enableFilter("paidFilter");
            paidFilter.setParameter("paid", paid);
        }
        if (rangeStart != null && !rangeStart.equalsIgnoreCase("null")) {
            Filter rangeStartFilter = session.enableFilter("rangeStartFilter");
            rangeStartFilter.setParameter("rangeStart", LocalDateTime.parse(rangeStart, dateFormat));
            dateRangeExists = true;
        }
        if (rangeEnd != null && !rangeEnd.equalsIgnoreCase("null")) {
            Filter rangeEndFiler = session.enableFilter("rangeEndFilter");
            rangeEndFiler.setParameter("rangeEnd", LocalDateTime.parse(rangeEnd, dateFormat));
            dateRangeExists = true;
        }
        if (!dateRangeExists) {
            Filter rangeEndFiler = session.enableFilter("rangeStartFilter");
            rangeEndFiler.setParameter("rangeStart", LocalDateTime.now());
        }
        if (onlyAvailable) {
            session.enableFilter("approvedRequestsFilter");
        }
        if (placeId != null) {
            Place place = placeRepository.findById(placeId).orElseThrow(() ->
                    new NotFoundException("место с id=" + placeId + " не найдено."));

            Filter distanceFilter = session.enableFilter("distanceFilter");
            distanceFilter.setParameter("placeLat", place.getLocation().getLat());
            distanceFilter.setParameter("placeLon", place.getLocation().getLon());
            distanceFilter.setParameter("radius", place.getLocation().getRadius());

        }
        List<Event> events = eventRepository.findAll(sortedPaging).getContent();
        session.disableFilter("categoryFilter");
        session.disableFilter("textFilter");
        session.disableFilter("rangeStartFilter");
        session.disableFilter("rangeEndFilter");
        session.disableFilter("paidFilter");
        session.disableFilter("stateFilter");
        session.disableFilter("approvedRequestsFilter");

        List<EventShortDto> eventDtos = events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        Map<Long, Long> viewMap = getViews(new ArrayList<>(eventDtos));
        for (EventDto nextDto : eventDtos) {
            if ((viewMap.get(nextDto.getId()) != null)) {
                nextDto.setViews(viewMap.get(nextDto.getId()));
            }
        }
        if (sort.equalsIgnoreCase("VIEWS")) {
            return eventDtos.stream().sorted(Comparator.comparingLong(EventShortDto::getViews).reversed())
                    .collect(Collectors.toList());
        } else {
            return eventDtos;
        }
    }

    @Override
    @Transactional
    public EventFullDto cancelByUser(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " для пользователя userId=" + userId + " не найдено."));
        if (!(event.getState() == EventState.PENDING)) {
            throw new WrongActionException("отменить можно только событие в ожидании модерации. Событие с id=" + eventId +
                    " имеет статус " + event.getState().name());
        }
        event.setState(EventState.CANCELED);

        EventFullDto dto = EventMapper.toEventFullDto(eventRepository.save(event));
        dto.setViews(getViewsSingle(dto));
        return dto;
    }

    @Override
    public EventFullDto findByIdAndUser(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " и userId=" + userId + " не найдено."));

        EventFullDto dto = EventMapper.toEventFullDto(event);
        dto.setViews(getViewsSingle(dto));
        return dto;
    }

    @Override
    @Transactional
    public EventFullDto publish(Long eventId) {
        Event pendingEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено."));
        if (!(EventState.PENDING.name().equalsIgnoreCase(pendingEvent.getState().name()))) {
            throw new WrongActionException("Только события со статусом PENDING могут быть опубликованы. " +
                    "Статус события id=" + eventId + " = " + pendingEvent.getState().name());
        }
        if (pendingEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new WrongActionException("Нельзя опубликовать событие позже чем за час до его начала. " +
                    "Дата события id=" + eventId + " = " + dateFormat.format(pendingEvent.getEventDate()));
        }
        pendingEvent.setState(EventState.PUBLISHED);
        pendingEvent.setPublishedOn(LocalDateTime.now());
        EventFullDto dto = EventMapper.toEventFullDto(eventRepository.save(pendingEvent));
        dto.setViews(getViewsSingle(dto));
        return dto;
    }

    @Override
    @Transactional
    public EventFullDto reject(Long eventId) {
        Event pendingEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено."));
        if (!EventState.PENDING.equals(pendingEvent.getState())) {
            return EventMapper.toEventFullDto(pendingEvent);
        }
        pendingEvent.setState(EventState.CANCELED);
        EventFullDto dto = EventMapper.toEventFullDto(eventRepository.save(pendingEvent));
        dto.setViews(getViewsSingle(dto));
        return dto;
    }

    @Override
    public List<EventShortDto> findByInitiator(Long userId, Integer from, Integer size) {
        Pageable sortedPaging = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
        List<EventShortDto> eventDtos = eventRepository.findAllByInitiator_Id(userId, sortedPaging)
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        Map<Long, Long> viewMap = getViews(new ArrayList<>(eventDtos));
        for (EventDto nextDto : eventDtos) {
            if ((viewMap.get(nextDto.getId()) != null)) {
                nextDto.setViews(viewMap.get(nextDto.getId()));
            }
        }

        return eventDtos;
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId) {

        ParticipationRequest request = requestRepository.findByIdAndEvent(requestId, eventId).orElseThrow(() ->
                new NotFoundException("Запрос с id=" + requestId + " для события " + eventId + " не найден."));
        if (request.getRequester().longValue() == userId.longValue()) {
            throw new WrongActionException("нельзя подтвердить собственный запрос на событие.");
        }
        if (!RequestStatus.PENDING.name().equalsIgnoreCase(request.getStatus().name())) {
            log.warn("можно подтвердить заявку только в статусе PENDING. Статус текущей заявки: "
                    + request.getStatus().name());
            return RequestMapper.toRequestDto(request);
        }

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не явлестя инициатором для события " + eventId +
                        " . Подтверждение участия невозможно."));

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            return RequestMapper.toRequestDto(requestRepository.save(request));
        } else {
            if (event.getApprovedRequestsCount() < event.getParticipantLimit()) {
                request.setStatus(RequestStatus.CONFIRMED);
                return RequestMapper.toRequestDto(requestRepository.save(request));
            } else {
                List<ParticipationRequest> pendingRequests = requestRepository.findAllByEventAndStatus(event.getId(), RequestStatus.PENDING);
                pendingRequests.stream().forEach(r -> r.setStatus(RequestStatus.REJECTED));
                requestRepository.saveAll(pendingRequests);
                ParticipationRequest resultRequest = requestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new NotFoundException("Запрос с id=" + requestId + " для события " + eventId + " не найден."));
                return RequestMapper.toRequestDto(resultRequest);
            }
        }

    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndEvent(requestId, eventId).orElseThrow(() ->
                new NotFoundException("Запрос с id=" + requestId + " для события " + eventId + " не найден."));
        if (request.getRequester().longValue() == userId.longValue()) {
            throw new WrongActionException("нельзя подтвердить собственный запрос на событие.");
        }
        eventRepository.findByIdAndInitiator_Id(eventId, userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id=" + userId + " не явлестя инициатором для события " + eventId +
                        " . Отклонение участия невозможно."));
        if (!RequestStatus.PENDING.equals(request.getStatus())) {
            return RequestMapper.toRequestDto(request);
        }
        request.setStatus(RequestStatus.REJECTED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findEventRequestsForUser(Long userId, Long eventId) {
        List<ParticipationRequest> requests = requestRepository.searchByEventInitiator(eventId, userId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto findByIdForPublic(Long eventId) {
        Session session = entityManager.unwrap(Session.class);
        Filter stateFilter = session.enableFilter("stateFilter");
        stateFilter.setParameter("states", EventState.PUBLISHED.name());
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено."));
        session.disableFilter("stateFilter");
        eventRepository.save(event);
        EventFullDto dto = EventMapper.toEventFullDto(event);
        dto.setViews(getViewsSingle(dto));
        return dto;
    }

    private Map<Long, Long> getViews(List<EventDto> events) {
        Map<Long, Long> eventHits = new HashMap<>();
        List<Long> ids = events.stream().map(e -> e.getId()).collect(Collectors.toList());
        if (ids.size() > 0) {
            List<ViewStats> stats = statsClient.getStats("1990-01-01 10:00:00",
                    "2030-01-01 10:00:00", ids, false);
            for (ViewStats view : stats) {
                String endPoint = view.getUri();
                String eventId = endPoint.substring(endPoint.lastIndexOf("/") + 1);
                if (eventId.length() > 0) {
                    eventHits.put(Long.parseLong(eventId), view.getHits());
                }
            }
        }
        return eventHits;
    }

    private long getViewsSingle(EventDto event) {
        List<Long> ids = new ArrayList<>();
        ids.add(event.getId());
        List<ViewStats> stats = (List<ViewStats>) statsClient.getStats("1990-01-01 10:00:00",
                "2030-01-01 10:00:00", ids, false);
        if (stats.size() == 0) {
            return 0;
        } else {
            return stats.get(0).getHits();
        }

    }


}
