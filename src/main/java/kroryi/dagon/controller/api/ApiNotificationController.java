package kroryi.dagon.controller.api;

import kroryi.dagon.DTO.NotificationDTO;
import kroryi.dagon.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class ApiNotificationController {

    private final NotificationService notificationService;

    // 예약알림 등록
    @PostMapping
    public ResponseEntity<NotificationDTO> create(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createNotification(dto));
    }

    // 공지사항,답변
    @PostMapping("/simple")
    public ResponseEntity<NotificationDTO> createSimple(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createSimpleNotification(dto));
    }

    // 알림 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotification(id));
    }

    //특정 유저의 알림 목록 조회
    @GetMapping("/user/{receiverId}")
    public ResponseEntity<List<NotificationDTO>> getByUser(@PathVariable Long receiverId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(receiverId));
    }

    // 알림 읽음 처리
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // 알림 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}