package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Data
@Table(name = "question_answers", schema = "dagon")
public class QuestionAnswers {
    @Id
    @Column(name = "qaid", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qid")
    private Question qid;

    @Column(name = "qacontent", nullable = false)
    private String qacontent;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "created_at")
    private Instant createdAt;


