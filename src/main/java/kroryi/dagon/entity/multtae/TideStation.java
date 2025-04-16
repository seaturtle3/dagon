package kroryi.dagon.entity.multtae;

import jakarta.persistence.*;
import kroryi.dagon.enums.ProdRegion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tide_station")
public class TideStation {
    @Id
    @Column(name = "station_code")
    private String stationCode;

    @Column(name = "station_name")
    private String stationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private ProdRegion region;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
