package ru.practicum.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PLACES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon")),
            @AttributeOverride(name = "radius", column = @Column(name = "radius"))
    })
    private Location location;

}
