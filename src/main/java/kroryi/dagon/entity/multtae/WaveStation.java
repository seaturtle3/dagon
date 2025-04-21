package kroryi.dagon.entity.multtae;

import jakarta.persistence.*;
import kroryi.dagon.enums.ProdRegion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "wave_station")
public class WaveStation {

    @Id
    @Column(name = "station_code")
    private String stationCode;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private ProdRegion region;
}