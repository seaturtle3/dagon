package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "faq")
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faqid", nullable = false)
    private Long faqId;

    @Column(name = "aid", nullable = false)
    private Long aid;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @Column(name = "question", nullable = false)
    private String question;

}