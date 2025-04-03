package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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

    @Enumerated
    @Column(name = "category_type")
    private Category_type categoryType;

    public enum Category_type {
        admin, partner, normal_user
    }

}
