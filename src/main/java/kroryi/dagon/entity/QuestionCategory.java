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
    private Long qcid;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private CategoryType categoryType;
    public enum CategoryType {
        USERE("사용자"),
        PARTNER("파트너"),
        ADMIN("관리자");

        private final String korean;

        CategoryType(String koreanName) {
            this.korean = koreanName;
        }

        public String getKorean() {
            return korean;
        }
    }

    @Column(name = "qcname", nullable = false)
    private String qcname;

}