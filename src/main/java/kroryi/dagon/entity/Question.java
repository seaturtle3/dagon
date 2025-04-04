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


    @Column(name = "qtype", nullable = false)
    private String qtype;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "qcontent", nullable = false)
    private String qcontent;

    @Column(name = "qtitle", nullable = false)
    private String qtitle;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private Usertype userType;
    public enum Usertype {
        USERE, PARTNER, ADMIN;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qcid", nullable = false)
    private kroryi.dagon.entity.QuestionCategory qcid;
}