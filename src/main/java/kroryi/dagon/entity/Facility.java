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
@Table(name = "facility", schema = "dagon")
public class Facility {

    @Id
    @Column(name = "faid", nullable = false)
    private Long faid;

    @Column(name = "faname", nullable = false)
    private String faname;

    @Column(name = "faicon_url", length = 500)
    private String faicon_url;

}
