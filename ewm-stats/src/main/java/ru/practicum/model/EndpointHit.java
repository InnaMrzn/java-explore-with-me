package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ENDPOINT_HITS")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class EndpointHit {

    public EndpointHit(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "APP", nullable = false)
    @NonNull
    private String app;
    @Column(name = "URI", nullable = false)
    @NonNull
    private String uri;
    @Column(name = "IP", nullable = false)
    @NonNull
    private String ip;
    @Column(name = "HIT_TIMESTAMP", nullable = false)
    @NonNull
    private LocalDateTime timestamp;

    @Transient
    private Long hits;
}

