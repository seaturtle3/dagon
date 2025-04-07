package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qid", nullable = false)
    private Long qid;


    @Column(name = "q_type", nullable = false)
    private String qType;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "q_created_at")
    private Instant qCreatedAt;

    @Column(name = "q_content", nullable = false)
    private String qContent;

    @Column(name = "q_title", nullable = false)
    private String qTitle;

    @Column(name = "q_updated_at")
    private Instant qUpdatedAt;

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