package kroryi.dagon.controller.common.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.InquiryCreateRequestDTO;
import kroryi.dagon.DTO.InquiryResponseDTO;
import kroryi.dagon.DTO.InquiryUpdateRequestDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.InquiryRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.support.InquiryService;
import kroryi.dagon.service.support.NotificationService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Tag(name = "User-Inquiry", description = "1:1 문의 API (사용자)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class ApiInquiryController {

    private final InquiryService inquiryService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;
    private final InquiryRepository inquiryRepository;


    // 1. 문의 생성
    @PostMapping
    public ResponseEntity<InquiryResponseDTO> createInquiry(
            @RequestBody @Valid InquiryCreateRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        InquiryResponseDTO response = inquiryService.createInquiry(userDetails.getUno(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 문의 리스트 조회 (검색+페이징)
    @GetMapping("/admin")
    public ResponseEntity<Page<InquiryResponseDTO>> getAdminInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<InquiryResponseDTO> responses = inquiryService.getAdminInquiries(pageable, keyword);

        return ResponseEntity.ok(responses);
    }

    // 3. 문의 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<InquiryResponseDTO> getInquiry(@PathVariable Long id) {
        InquiryResponseDTO response = inquiryService.getInquiry(id);
        return ResponseEntity.ok(response);
    }

    // 4. 문의 수정
    @PutMapping("/{id}")
    public ResponseEntity<InquiryResponseDTO> updateInquiry(
            @PathVariable Long id,
            @RequestBody @Valid InquiryUpdateRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        InquiryResponseDTO response = inquiryService.updateInquiry(userDetails.getUno(), id, request);
        return ResponseEntity.ok(response);
    }

    // 5. 문의 삭제
    @DeleteMapping("" +
            "/{inquiryId}")
    public ResponseEntity<?> deleteInquiry(
            @PathVariable Long inquiryId,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");

            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            Claims claims = jwtUtil.parseToken(token);
            String role = claims.get("role", String.class);

            boolean deleted;

            if ("ADMIN".equals(role)) {
                // 관리자면 누구 문의든 삭제 가능
                deleted = inquiryService.deleteInquiryByAdmin(inquiryId);
            } else if ("USER".equals(role)) {
                System.out.println("role: " + role);
                // 일반 사용자면 본인 문의만 삭제 가능
                Long uno = Long.parseLong(claims.get("uno").toString());
                deleted = inquiryService.deleteInquiryByUser(inquiryId, uno);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            if (deleted) {
                return ResponseEntity.ok("1:1 문의가 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 1:1 문의를 삭제할 권한이 없습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("1:1 문의 삭제 중 오류 발생");
        }
    }

    @PostMapping("/{inquiryId}/answer")
    public ResponseEntity<?> answerInquiry(
            @PathVariable Long inquiryId,
            @RequestBody Map<String, String> request
    ) {
        String answerContent = request.get("answerContent");

        // 관리자 sender는 null 처리하거나 직접 생성하거나 DB에서 ADMIN 계정 조회해도 되고
        // 만약 관리자 정보를 DB에서 조회할 필요 없고, 그냥 ADMIN으로 고정한다면 아래처럼 간단히 처리 가능
        User admin = null; // 혹은 userRepository.findByUid("admin") 등으로 관리자를 직접 조회

        // 문의 조회
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

        // 답변 저장
        inquiry.setAnswerContent(answerContent);
        inquiry.setAnswered(true);
        inquiry.setAnsweredAt(LocalDateTime.now());
        inquiryRepository.save(inquiry);

        // 알림 전송 (admin이 null이어도 senderType으로 ADMIN 구분 가능)
        notificationService.sendInquiryAnswerNotification(inquiry, admin);

        return ResponseEntity.ok("답변이 저장되고 알림이 전송되었습니다.");
    }
    @GetMapping("/user-to-partner")
    public ResponseEntity<List<Inquiry>> getUserToPartnerInquiries(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long partnerUno
    ) {
        Long userUno = userDetails.getUno(); // 토큰에서 유저 uno 추출
        List<Inquiry> inquiries = inquiryService.getUserToPartnerInquiries(userUno, partnerUno);
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/partner-inquiries")
    public ResponseEntity<List<InquiryResponseDTO>> getInquiriesToPartner(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long partnerUno) {

        // 현재 로그인한 파트너가 요청한 것인지 확인하려면 아래 로직을 사용할 수 있음
        if (!userDetails.getUno().equals(partnerUno)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<InquiryResponseDTO> inquiries = inquiryService.getInquiriesToPartner(partnerUno);
        return ResponseEntity.ok(inquiries);
    }

}
