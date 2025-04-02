package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class QuestionAnswers {
    @Id
    @Column(name = "qaid", nullable = false)
    private Long qaid;

    @ManyToOne
    @JoinColumn
    private Question qid;

    @Column(name = "qacontent", nullable = false)
    private String qacontent;

    @Column(name = "answer_id")
    private Long answer_id;

    @Column(name = "created_at")
    private LocalDateTime created_at;

}
