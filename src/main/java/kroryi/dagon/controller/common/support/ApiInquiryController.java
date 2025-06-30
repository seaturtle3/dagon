package kroryi.dagon.controller.common.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.InquiryCreateRequestDTO;
import kroryi.dagon.DTO.InquiryResponseDTO;
import kroryi.dagon.DTO.InquiryUpdateRequestDTO;
import kroryi.dagon.DTO.PartnerInquiryCreateRequestDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.User;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.repository.InquiryRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.product.ProductRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

@Tag(name = "User-Inquiry", description = "1:1 문의 API (사용자)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
@Log4j2
public class ApiInquiryController {

    private final InquiryService inquiryService;
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;
    private final InquiryRepository inquiryRepository;
    private final ProductRepository productRepository;

    // 1. 문의 생성
    @Operation(summary = "1:1문의 생성", description = "사용자가 1:1 문의를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "문의 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createInquiry(
            @RequestBody @Valid InquiryCreateRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            Long uno = userDetails.getUno();
            log.info("문의 생성 요청 - 사용자 ID: {}, 제목: {}", uno, request.getTitle());

            InquiryResponseDTO response = inquiryService.createInquiry(uno, request);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "문의가 성공적으로 등록되었습니다.");
            result.put("data", response);

            log.info("문의 생성 완료 - 문의 ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            log.error("문의 생성 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의 등록 중 오류가 발생했습니다: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 2. 문의 리스트 조회 (검색+페이징)
    @Operation(summary = "1:1문의 리스트 조회", description = "관리자용 문의 리스트를 페이징과 검색으로 조회합니다.")
    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getAdminInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String inquiryType
    ) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<InquiryResponseDTO> responses = inquiryService.getAdminInquiries(pageable, keyword, status, inquiryType);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", responses);
            result.put("totalElements", responses.getTotalElements());
            result.put("totalPages", responses.getTotalPages());
            result.put("currentPage", page);
            result.put("pageSize", size);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("문의 리스트 조회 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의 리스트 조회 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 3. 문의 단건 조회
    @Operation(summary = "1:1문의 단건 조회", description = "특정 문의의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getInquiry(@PathVariable Long id) {
        try {
            InquiryResponseDTO response = inquiryService.getInquiry(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("문의 조회 중 오류 발생 - 문의 ID: {}, 오류: {}", id, e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의를 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // 4. 문의 수정
    @Operation(summary = "1:1문의 수정", description = "사용자가 자신의 문의를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateInquiry(
            @PathVariable Long id,
            @RequestBody @Valid InquiryUpdateRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            InquiryResponseDTO response = inquiryService.updateInquiry(userDetails.getUno(), id, request);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "문의가 성공적으로 수정되었습니다.");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (AccessDeniedException e) {
            log.warn("문의 수정 권한 없음 - 사용자 ID: {}, 문의 ID: {}", userDetails.getUno(), id);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "해당 문의를 수정할 권한이 없습니다.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

        } catch (Exception e) {
            log.error("문의 수정 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의 수정 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 5. 문의 삭제
    @Operation(summary = "1:1문의 삭제", description = "사용자, 파트너, 관리자가 문의를 삭제합니다.")
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Map<String, Object>> deleteInquiry(
            @PathVariable Long inquiryId,
            @RequestHeader("Authorization") String authorization) {

        try {
            String token = authorization.replace("Bearer ", "");

            if (!jwtUtil.isValidToken(token)) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "유효하지 않은 토큰입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            Claims claims = jwtUtil.parseToken(token);
            String role = claims.get("role", String.class);
            boolean deleted = false;

            if ("ADMIN".equals(role)) {
                deleted = inquiryService.deleteInquiryByAdmin(inquiryId);
            } else if ("USER".equals(role)) {
                Long uno = Long.parseLong(claims.get("uno").toString());
                deleted = inquiryService.deleteInquiryByUser(inquiryId, uno);
            } else if ("PARTNER".equals(role)) {
                Long uno = Long.parseLong(claims.get("uno").toString());
                deleted = inquiryService.deleteInquiryByPartner(inquiryId, uno);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            Map<String, Object> result = new HashMap<>();
            if (deleted) {
                result.put("success", true);
                result.put("message", "문의가 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "해당 문의를 삭제할 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
            }

        } catch (Exception e) {
            log.error("문의 삭제 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의 삭제 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 6. 문의 답글 저장 및 전송
    @Operation(summary = "1:1문의 답글/알림", description = "관리자가 문의에 답변을 작성하고 알림을 전송합니다.")
    @PostMapping("/{inquiryId}/answer")
    public ResponseEntity<Map<String, Object>> answerInquiry(
            @PathVariable Long inquiryId,
            @RequestBody Map<String, String> request) {

        try {
            String answerContent = request.get("answerContent");

            if (answerContent == null || answerContent.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "답변 내용을 입력해주세요.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            User admin = null; // 관리자 정보는 필요에 따라 조회

            Inquiry inquiry = inquiryRepository.findById(inquiryId)
                    .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

            inquiry.setAnswerContent(answerContent);
            inquiry.setAnswered(true);
            inquiry.setAnsweredAt(LocalDateTime.now());
            inquiryRepository.save(inquiry);

            notificationService.sendInquiryAnswerNotification(inquiry, admin);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "답변이 저장되고 알림이 전송되었습니다.");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("문의 답변 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "답변 저장 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 7. 사용자-파트너 간 문의 조회
    @GetMapping("/user-to-partner")
    public ResponseEntity<Map<String, Object>> getUserToPartnerInquiries(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long partnerUno) {

        try {
            Long userUno = userDetails.getUno();
            List<Inquiry> inquiries = inquiryService.getUserToPartnerInquiries(userUno, partnerUno);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", inquiries);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("사용자-파트너 문의 조회 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의 조회 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 8. 파트너에게 온 문의 조회
    @GetMapping("/partner-inquiries")
    public ResponseEntity<Map<String, Object>> getInquiriesToPartner(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long partnerUno) {

        try {
            if (!userDetails.getUno().equals(partnerUno)) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            List<InquiryResponseDTO> inquiries = inquiryService.getInquiriesToPartner(partnerUno);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", inquiries);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("파트너 문의 조회 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의 조회 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 9. 내 문의 목록 조회
    @GetMapping("/my-inquiries")
    public ResponseEntity<Map<String, Object>> getMyInquiries(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        try {
            Long userUno = currentUser.getUno();
            List<InquiryResponseDTO> inquiries = inquiryService.getInquiriesByUserUno(userUno);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", inquiries);
            result.put("count", inquiries.size());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("내 문의 목록 조회 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "문의 목록 조회 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 10. 파트너 전용 1:1 문의 생성 (상품ID로 파트너 자동 매핑)
    @Operation(summary = "파트너 전용 1:1 문의 생성", description = "상품ID로 파트너를 자동 매핑하여 문의를 생성합니다.")
    @PostMapping("/partner")
    public ResponseEntity<Map<String, Object>> createPartnerInquiry(
            @RequestBody PartnerInquiryCreateRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

            Long partnerUno = product.getPartner().getUno();

            InquiryCreateRequestDTO inquiryRequest = new InquiryCreateRequestDTO();
            inquiryRequest.setReceiverType(kroryi.dagon.enums.ReceiverType.PARTNER);
            inquiryRequest.setReceiverId(partnerUno);
            inquiryRequest.setPartnerId(partnerUno);
            inquiryRequest.setTitle(request.getTitle());
            inquiryRequest.setContent(request.getContent());
            inquiryRequest.setInquiryType(request.getInquiryType());
            inquiryRequest.setPartnerName(product.getPartner().getPname());
            inquiryRequest.setWriterType("USER");

            InquiryResponseDTO response = inquiryService.createInquiry(userDetails.getUno(), inquiryRequest);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "파트너 문의가 성공적으로 등록되었습니다.");
            result.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            log.error("파트너 문의 생성 중 오류 발생: {}", e.getMessage(), e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "파트너 문의 등록 중 오류가 발생했습니다: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
