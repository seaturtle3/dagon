package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fishing_gear", schema = "dagon")
public class FishingGear {

    @Id
    @Column(name = "gid", nullable = false)
    private Long gid;

    @Column(name = "gname", nullable = false)
    private String gname;

    @Column(name = "gicon_url", length = 500)
    private String giconUrl;

}
