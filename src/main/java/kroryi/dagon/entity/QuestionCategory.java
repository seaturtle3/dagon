package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class QuestionCategory {
    @Id
    @Column(name = "qcid", nullable = false)
    private Long qcid;

    @Column(name = "qcname", nullable = false)
    private String qcname;

    @Column(name = "category_type", nullable = false)
    private Enum category_type;

}
