package ru.practicum.service;

import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitMapper;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@NoArgsConstructor
public class StatsService {

    private static final DateTimeFormatter dateFormat
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EntityManager entityManager;

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, String unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, dateFormat);
        LocalDateTime endTime = LocalDateTime.parse(end, dateFormat);
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<EndpointHit> cr = cb.createQuery(EndpointHit.class);
        Root<EndpointHit> c = cr.from(EndpointHit.class);
        cr.groupBy(c.get("app"), c.get("uri"));
        cr.where(
                cb.greaterThan(c.get("timestamp"), startTime),
                cb.lessThan(c.get("timestamp"), endTime)
        );
        if (uris != null) {
            cr.select(c).where(c.get("uri").in(uris));
        }
        if (unique != null && !unique.equalsIgnoreCase("null") && Boolean.parseBoolean(unique)) {
            cr.multiselect(c.get("app"), c.get("uri"), cb.countDistinct(c.get("ip")));
        } else {
            cr.multiselect(c.get("app"), c.get("uri"), cb.count(c.get("ip")));
        }
        Query<EndpointHit> query = session.createQuery(cr);
        List<EndpointHit> results = query.getResultList();
        return results.stream().map(EndpointHitMapper::toView).collect(Collectors.toList());
    }
}
