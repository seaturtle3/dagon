package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FAQ {
    @Id
    @Column(name = "faquid", nullable = false)
    private Long faquid;

    @Column(name = "aid", nullable = false)
    private Long aid;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modify_at")
    private LocalDateTime modifiedAt;

}
