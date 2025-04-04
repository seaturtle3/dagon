package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "question_answers")
public class QuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qaid", nullable = false)
    private Long id;

    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "qacontent", nullable = false)
    private String qacontent;

    @Column(name = "qid_qid")
    private Long qidQid;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qid")
    private Question qid;

}