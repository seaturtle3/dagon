package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "qtype", nullable = false)
    private Qtype qtype;

    public enum Qtype {
        reservation, cancellation
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "usertype", nullable = false)
    private Usertype usertype;

    public enum Usertype {
        user, partner, admin;
    }

    @Column(name = "qtitle", nullable = false)
    private String qtitle;

    @Column(name = "qcontent", nullable = false)
    private String qcontent;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;


}
