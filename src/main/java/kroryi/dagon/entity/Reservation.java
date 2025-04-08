package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "uid", nullable = false)
    private Long uid;


    @Column(name = "prod_id", nullable = false)
    private Long prodId;

    @Column(name = "prod_region", nullable = false)
    private String prodRegion;

    @Column(name = "fish_type", nullable = false)
    private String fishType;

    @Column(name = "opt_id", nullable = false)
    private Long optId;


    @Column(name = "num_person", nullable = false)
    private Integer numPerson;

    @Column(name = "reservation_at", nullable = false)
    private LocalDateTime reservationAt;

    @Column(name = "fishing_at", nullable = false)
    private LocalDateTime fishingAt;





}