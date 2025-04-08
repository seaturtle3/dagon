package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

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
    private String rid;

    @Column(name = "prod_id", nullable = false)
    private String prodId;

    @Column(name = "option_id", nullable = false)
    private String optionId;

    @Column(name = "uid", nullable = false)
    private String uid;

    @Column(name = "reservation_at", nullable = false)
    private LocalDate reservationAt;

    @Column(name = "fishing_at", nullable = false)
    private LocalDate fishingAt;

    @Column(name = "num_person", nullable = false)
    private Integer numPerson;

    @Column(name = "fish_type")
    private String fishType;

}