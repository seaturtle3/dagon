package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "question_answers", schema = "dagon")
public class QuestionAnswers {
    @Id
    @Column(name = "qaid", nullable = false)
    private Long qaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qid")
    private Question qid;

    @Column(name = "qacontent", nullable = false)
    private String qacontent;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

