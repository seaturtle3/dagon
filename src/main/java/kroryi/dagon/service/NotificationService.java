package kroryi.dagon.service;

import kroryi.dagon.DTO.NotificationDTO;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.Notification;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.SenderType;
import kroryi.dagon.repository.NotificationRepository;

import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SeaFreshwaterFishingRepository reservationRepository;

    // 예약 정보 처리
    public NotificationDTO createNotification(NotificationDTO dto) {
        // uid 기준으로 유저 조회
        User receiver = userRepository.findByUid(dto.getReceiverUid())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        User sender = null;
        SenderType senderType = SenderType.fromString(String.valueOf(dto.getSenderType()));

        // 발신자 처리 (생략 또는 필요 시 sender도 같은 방식으로 조회)

        Reservation reservation = null;
        if (dto.getReservationId() != null) {
            reservation = reservationRepository.findById(dto.getReservationId())
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));
        }

        Notification notification = new Notification();
        notification.setReceiver(receiver);
        notification.setSender(sender);
        notification.setSenderType(senderType);
        notification.setType(dto.getType());
        notification.setTitle(dto.getTitle());
        notification.setContent(dto.getContent());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReservation(reservation);

        Notification saved = notificationRepository.save(notification);
        return convertToDTO(saved);
    }

    public NotificationDTO createSimpleNotification(NotificationDTO dto) {
        User receiver = userRepository.findByUid(dto.getReceiverUid())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        User sender = null;
        SenderType senderType = SenderType.fromString(String.valueOf(dto.getSenderType()));

        if (senderType == SenderType.ADMIN || senderType == SenderType.PARTNER) {
            sender = userRepository.findByUid(dto.getReceiverUid())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }

        Notification notification = new Notification();
        notification.setReceiver(receiver);
        notification.setSender(sender);
        notification.setSenderType(senderType);
        notification.setType(dto.getType()); // NOTICE 또는 REPLY
        notification.setTitle(dto.getTitle());
        notification.setContent(dto.getContent());
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return convertToDTO(saved);
    }

    public NotificationDTO getNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return convertToDTO(notification);
    }


    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        Notification updated = notificationRepository.save(notification);
        return convertToDTO(updated);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO convertToDTO(Notification entity) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setReceiverUid(entity.getReceiver().getUid());
        dto.setSenderUid(entity.getSender() != null ? entity.getSender().getUid() : null);
        dto.setSenderType(entity.getSenderType());
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setSenderName(entity.getSender() != null ? entity.getSender().getUname() : "SYSTEM");
        dto.setRead(entity.isRead());
        dto.setCreatedAt(entity.getCreatedAt().toString());
        return dto;
    }

    public void sendAdminNotification(NotificationDTO dto) {
        List<User> receivers;

        if (dto.getReceiverUid() != null) {
            User receiver = userRepository.findByUid(dto.getReceiverUid())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
            receivers = List.of(receiver);
        } else {
            receivers = userRepository.findAll(); // 전체 발송
        }

        for (User receiver : receivers) {
            Notification notification = new Notification();
            notification.setReceiver(receiver);
            notification.setSender(null); // 관리자 시스템 발송이므로 null
            notification.setSenderType(SenderType.ADMIN);
            notification.setType(dto.getType());
            notification.setTitle(dto.getTitle());
            notification.setContent(dto.getContent());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);

            notificationRepository.save(notification);
        }
    }

    public List<NotificationDTO> getNotificationsByUser(String receiverUid) {
        return notificationRepository.findByReceiver_UidOrderByCreatedAtDesc(receiverUid)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<NotificationDTO> getNotifications(String uid, String type, Pageable pageable) {
        // 빈 문자열("")은 null로 변환해서 처리
        if (uid != null && uid.isBlank()) {
            uid = null;
        }
        if (type != null && (type.isBlank() || type.equals("전체"))) {
            type = null;
        }

        Page<Notification> notifications;

        if (uid != null && type != null) {
            notifications = notificationRepository.findByReceiver_UidAndType(uid, type, pageable);
        } else if (uid != null) {
            notifications = notificationRepository.findByReceiver_Uid(uid, pageable);
        } else if (type != null) {
            notifications = notificationRepository.findByType(type, pageable);
        } else {
            notifications = notificationRepository.findAll(pageable);
        }

        return notifications.map(this::convertToDTO);
    }

    public void sendInquiryAnswerNotification(Inquiry inquiry, User sender) {
        Notification notification = new Notification();
        notification.setReceiver(inquiry.getUser());  // 수신자 - 문의한 유저
        notification.setSender(sender);                // 보낸 사람 - null이어도 무관
        notification.setSenderType(SenderType.ADMIN); // 보낸 사람 타입으로 관리자 구분
        notification.setType("ANSWER");
        notification.setTitle("1:1 문의 답변 도착");
        notification.setContent(inquiry.getAnswerContent());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
}
