package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fishingReportImage", schema = "dagon")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Frimg {
    @Id
    @Column(name = "frimg_id", nullable = false)
    private Integer id;

    @Column(name = "frid")
    private Integer frid;

    @Column(name = "frimg_url", length = 512)
    private String frimgUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

}