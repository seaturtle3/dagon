package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Question {
    @Id
    @Column(name = "qid", nullable = false)
    private Long qid;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @ManyToOne
    @JoinColumn(name = "qcid")
    private QuestionCategory questionCategory;

    @Column(name = "qtype", nullable = false)
    private Enum qtype;

    @Column(name = "qtitle", nullable = false)
    private String qtitle;

    @Column(name = "qcontent", nullable = false)
    private String qcontent;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

}
