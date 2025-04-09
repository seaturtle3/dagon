package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.CategoryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question_category")
public class QuestionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qc_id", nullable = false)
    private Long qcId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private CategoryType categoryType;


    @Column(name = "qc_name", nullable = false)
    private String qcName;

}