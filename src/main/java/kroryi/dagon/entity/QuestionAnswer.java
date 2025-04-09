package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question_answers")
public class QuestionAnswer extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qa_id", nullable = false)
    private Long qaId;

    @Column(name = "answer_id")
    private Long answerId;


    @Column(name = "qa_content", nullable = false)
    private String qaContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

}