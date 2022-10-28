package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ParamDef;
import ru.practicum.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EVENTS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@FilterDef(name = "textFilter", parameters = @ParamDef(name = "text", type = "text"))

@FilterDef(name = "userFilter", parameters = @ParamDef(name = "users", type = "long"))
@Filter(name = "userFilter", condition = "initiator_id IN (:users)")

@FilterDef(name = "paidFilter", parameters = @ParamDef(name = "paid", type = "boolean"))
@Filter(name = "paidFilter", condition = "paid IN (:paid)")

@FilterDef(name = "stateFilter", parameters = @ParamDef(name = "states", type = "text"))
@Filter(name = "stateFilter", condition = "event_state IN (:states)")

@FilterDef(name = "categoryFilter", parameters = @ParamDef(name = "categories", type = "long"))
@Filter(name = "categoryFilter", condition = "category_id IN (:categories)")

@FilterDef(name = "rangeStartFilter", parameters = @ParamDef(name = "rangeStart", type = "LocalDateTime"))
@Filter(name = "rangeStartFilter", condition = "event_date >= :rangeStart")

@FilterDef(name = "rangeEndFilter", parameters = @ParamDef(name = "rangeEnd", type = "LocalDateTime"))
@Filter(name = "rangeEndFilter", condition = "event_date <= :rangeEnd")
@Filter(name = "textFilter", condition = "(lower(description) LIKE lower(concat('%', :text,'%'))" +
        " OR lower(annotation) LIKE lower(concat('%', :text,'%')))")

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "ANNOTATION", nullable = false)
    private String annotation;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "EVENT_DATE", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "PUBLISHED_DATE", nullable = false)
    private LocalDateTime publishedOn;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon")),
            @AttributeOverride(name = "radius", column = @Column(name = "radius"))
    })
    private Location location;
    @Column(name = "PAID", nullable = false)
    private boolean paid;
    @Column(name = "REQUEST_MODERATION", nullable = false)
    private boolean requestModeration;
    @Column(name = "PARTS_LIMIT", nullable = false)
    private int participantLimit;
    @Enumerated(EnumType.STRING)
    @Column(name = "event_state", nullable = false)
    private EventState state;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "events")
    @JsonIgnore
    private final List<Compilation> compilations = new ArrayList<>();
    @Column(name = "views")
    private long views;

    @Formula("(select count(r.id) from requests r " +
            "where r.event_id =id and upper(r.request_status)='CONFIRMED') ")
    private long approvedRequestsCount;


}
