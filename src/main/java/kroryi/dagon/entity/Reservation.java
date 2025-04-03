package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid", nullable = false)
    private Long id;

    @Column(name = "prod_id", nullable = false)
    private Integer prodId;

    @Column(name = "oid", nullable = false)
    private Long oid;

    @Column(name = "uid", nullable = false)
    private Integer uid;

    @Column(name = "reservation_at", nullable = false)
    private Instant reservationAt;

    @Column(name = "fishing_at", nullable = false)
    private LocalDate fishingAt;

    @Column(name = "num_person", nullable = false)
    private Integer numPerson;

}