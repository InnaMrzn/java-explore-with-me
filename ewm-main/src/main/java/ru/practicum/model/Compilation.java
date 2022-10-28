package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPILATIONS")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "PINNED", nullable = false)
    private Boolean pinned;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "EVENTS_COMPILATIONS",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        this.events.add(event);
        event.getCompilations().add(this);
    }

    public void removeEvent(long eventId) {
        Event event = this.events.stream().filter(e -> e.getId() == eventId).findFirst().orElse(null);
        if (event != null) {
            this.events.remove(event);
            event.getCompilations().remove(this);
        }
    }
}
