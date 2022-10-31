package ru.practicum.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "NAME", nullable = false)
    private String name;
    @NonNull
    @Column(name = "EMAIL", nullable = false)
    private String email;


}
