package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestionCategory {
    @Id
    @Column(name = "qcid", nullable = false)
    private Long qcid;

    @Column(name = "qcname", nullable = false)
    private String qcname;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "category_type", nullable = false)
//    private CategoryType categoryType;

}
