package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.enums.WriterType;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;
    private String contact;
    private String title;
    private String content;
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private InquiryType inquiryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private WriterType writerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    private ReceiverType receiverType;

}
