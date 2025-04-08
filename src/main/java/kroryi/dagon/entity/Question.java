package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qid", nullable = false)
    private Long qid;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "question_content", nullable = false)
    private String questionContent;

    @Column(name = "question_title", nullable = false)
    private String questionTitle;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private Questiontype questionType;
    public enum Questiontype {
        PRODUCT("상품문의"),
        RESERVATION("예약문의"),
        CANCLATION("예약취소문의"),
        SYSTEM("시스템문의"),
        BUSINESS("제휴문의");

        private final String korean;

        Questiontype(String koreanName) {
            this.korean = koreanName;
        }

        public String getKorean() {
            return korean;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "u_type", nullable = false)
    private Usertype uType;
    public enum Usertype {
        USERE, PARTNER, ADMIN;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qcid", nullable = false)
    private kroryi.dagon.entity.QuestionCategory qcid;
}