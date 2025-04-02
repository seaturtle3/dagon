package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fishspecies", schema = "dagon")
public class Fishspecies {

    @Id
    @Column(name = "fiid", nullable = false)
    private Long fiid;

    @Column(name = "finame", nullable = false)
    private String finame;

    @Column(name = "fiicon_url", length = 500)
    private String fiicon_url;

}
