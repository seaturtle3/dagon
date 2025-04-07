package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "faq")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_uid", nullable = false)
    private Long faqUid;

    @Column(name = "aid", nullable = false)
    private Long aid;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @Column(name = "question", nullable = false)
    private String question;

}