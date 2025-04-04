package kroryi.dagon.entity;

import jakarta.persistence.*;
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
    private Long id;

    @Column(name = "category_type", nullable = false)
    private String categoryType;

    @Column(name = "qcname", nullable = false)
    private String qcname;

}