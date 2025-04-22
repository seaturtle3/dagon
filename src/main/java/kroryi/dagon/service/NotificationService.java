package kroryi.dagon.service;

import kroryi.dagon.DTO.NotificationDTO;
import kroryi.dagon.entity.Notification;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.SenderType;
import kroryi.dagon.repository.NotificationRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final UserRepository userRepository;
        private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;  // 예약 정보 처리

        public NotificationDTO createNotification(NotificationDTO dto) {
            // 유저 정보 받아오기
            User receiver = userRepository.findById(dto.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            User sender = null;
            SenderType senderType = SenderType.fromString(String.valueOf(dto.getSenderType()));

            // 발신자 처리 (생략)

            // 예약 정보 받아오기
            Reservation reservation = null;
            if (dto.getReservationId() != null) {
                reservation = seaFreshwaterFishingRepository.findById(dto.getReservationId())
                        .orElseThrow(() -> new RuntimeException("Reservation not found"));
            }

            Notification notification = new Notification();
            notification.setReceiver(receiver);  // 유저 정보 연결
            notification.setSender(sender);      // 발신자 정보 연결
            notification.setSenderType(senderType);
            notification.setType(dto.getType());
            notification.setTitle(dto.getTitle());
            notification.setContent(dto.getContent());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setReservation(reservation);  // 예약 정보 연결

            Notification saved = notificationRepository.save(notification);
            return convertToDTO(saved);
        }

        public NotificationDTO createSimpleNotification(NotificationDTO dto) {
            User receiver = userRepository.findById(dto.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            User sender = null;
            SenderType senderType = SenderType.fromString(String.valueOf(dto.getSenderType()));

            if (senderType == SenderType.ADMIN || senderType == SenderType.PARTNER) {
                sender = userRepository.findById(dto.getSenderId())
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

    public List<NotificationDTO> getNotificationsByUser(Long receiverId) {
        return notificationRepository.findByReceiver_UnoOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
        dto.setReceiverId(entity.getReceiver().getUno());
        dto.setSenderId(entity.getSender() != null ? entity.getSender().getUno() : null);
        dto.setSenderType(entity.getSenderType());
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setSenderName(entity.getSender() != null ? entity.getSender().getUname() : "SYSTEM");
        dto.setRead(entity.isRead());
        dto.setCreatedAt(entity.getCreatedAt().toString());
        return dto;
    }
}