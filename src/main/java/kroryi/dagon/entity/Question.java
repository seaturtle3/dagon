package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.QuestionType;
import kroryi.dagon.enums.WriteType;
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
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "write_type", nullable = false)
    private WriteType writeType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qcid", nullable = false)
    private kroryi.dagon.entity.QuestionCategory qcid;
}