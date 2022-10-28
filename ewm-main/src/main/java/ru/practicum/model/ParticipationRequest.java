package ru.practicum.model;

import lombok.*;
import ru.practicum.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUESTS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime created;
    @Column(name = "EVENT_ID", nullable = false)
    private Long event;
    @Column(name = "REQUESTER_ID", nullable = false)
    private Long requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "REQUEST_STATUS", nullable = false)
    private RequestStatus status;
}
