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
    private Long qaid;

    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "qa_created_at")
    private Instant qaCreatedAt;

    @Column(name = "qa_content", nullable = false)
    private String qaContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qid")
    private Question qid;

}