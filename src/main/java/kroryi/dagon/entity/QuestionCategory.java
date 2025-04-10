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
    @Column(name = "qcid", nullable = false)
    private Long qcid;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private CategoryType categoryType;

    @Column(name = "qcname", nullable = false)
    private String qcname;

}