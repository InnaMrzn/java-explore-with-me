package ru.practicum.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORIES")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "NAME", nullable = false)
    private String name;
}
