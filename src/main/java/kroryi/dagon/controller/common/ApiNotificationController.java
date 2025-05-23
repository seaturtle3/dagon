package kroryi.dagon.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.NotificationDTO;
import kroryi.dagon.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 API (사용자 + 관리자)")
@RequestMapping("/api/notifications")
public class ApiNotificationController {

    private final NotificationService notificationService;

    // 예약알림 등록
    @PostMapping
    @Operation(summary = "예약알림 등록 ", description = "예약알림 등록")
    public ResponseEntity<NotificationDTO> create(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createNotification(dto));
    }

    // 공지사항,답변
    @PostMapping("/simple")
    @Operation(summary = "공지 등록 ", description = "공지 답변 등록")
    public ResponseEntity<NotificationDTO> createSimple(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createSimpleNotification(dto));
    }

    // 알림 단건 조회
    @GetMapping("/{id}")
    @Operation(summary = "알림 아이디로 알림 조회 ", description = "알림 아이디로 조회")
    public ResponseEntity<NotificationDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotification(id));
    }

    //특정 유저의 알림 목록 조회
    @GetMapping("/user/{receiverUid}")
    @Operation(summary = "유저 uno 조회", description = "유저 번호로 조회")
    public ResponseEntity<List<NotificationDTO>> getByUser(@PathVariable String receiverUid) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(receiverUid));
    }

    // 알림 읽음 처리
    @PutMapping("/{id}/read")
    @Operation(summary = "읽음처리용", description = "읽음처리용")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // 알림 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "알림 삭제 ", description = "알림 삭제")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin-broadcast")
    @Operation(summary = "관리자 알림 등록", description = "전체 유저 또는 특정 유저에게 알림 발송")
    public ResponseEntity<String> broadcast(@RequestBody NotificationDTO dto) {
        notificationService

                .sendAdminNotification(dto);
        return ResponseEntity.ok("알림이 성공적으로 전송되었습니다.");
    }


    @GetMapping
    @Operation(summary = "알림 전체/검색 조회", description = "검색 조건에 따라 알림 리스트 조회 (페이징 포함)")
    public ResponseEntity<Page<NotificationDTO>> getNotifications(
            @RequestParam(required = false) String uid,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (uid != null && uid.isBlank()) {
            uid = null;
        }
        if (type != null && (type.isBlank() || type.equals("전체"))) {
            type = null; // 필터링 조건 제거
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<NotificationDTO> notifications = notificationService.getNotifications(uid, type, pageable);
        return ResponseEntity.ok(notifications);
    }
}

