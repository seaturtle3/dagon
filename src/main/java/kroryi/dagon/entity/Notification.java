package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.SenderType;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 설정
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;; // 알림 받는 사람 (User 엔티티와 연결)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;  // 발신자 (User 엔티티로 변경, ADMIN, PARTNER, SYSTEM 구분)

    @Enumerated(EnumType.STRING)
    private SenderType senderType; // ADMIN, PARTNER, SYSTEM

    private String type;     // 알림 종류 (NOTICE, RESERVATION,ANSWER 등)
    private String title;
    private String content;
    private boolean isRead = false;

    private LocalDateTime createdAt;




}

