package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.QuestionType;
import kroryi.dagon.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "question_title", nullable = false)
    private String questionTitle;

    @Column(name = "question_content", nullable = false)
    private String questionContent;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;


    @Enumerated(EnumType.STRING)
    @Column(name = "u_type", nullable = false)
    private UserType uType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qc_id", nullable = false)
    private QuestionCategory questionCategory;
}