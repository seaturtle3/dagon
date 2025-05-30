package kroryi.dagon.controller.admin.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.KakaoPayDTO;
import kroryi.dagon.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Payments", description = "카카오페이 결제정보 API (관리자)")
@RequestMapping("/api/kakaopay")
@RequiredArgsConstructor
public class ApiAdminKakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping
    @Operation(summary = "결제 정보 생성", description = "카카오페이 결제 정보 생성")
    public ResponseEntity<KakaoPayDTO> create(@RequestBody KakaoPayDTO dto) {
        return ResponseEntity.ok(kakaoPayService.save(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "카카오 결제정보 읽기", description = "카카오페이 결제정보 읽기")
    public ResponseEntity<KakaoPayDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(kakaoPayService.get(id));
    }

    @GetMapping
    @Operation(summary = "카카오 전체 결제정보 조회", description = "카카오페이 전체 결제정보 조회")
    public ResponseEntity<List<KakaoPayDTO>> readAll() {
        return ResponseEntity.ok(kakaoPayService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "카카오 결제정보 업데이트", description = "카카오페이 결제정보 업데이트")
    public ResponseEntity<KakaoPayDTO> update(@PathVariable Long id, @RequestBody KakaoPayDTO dto) {
        return ResponseEntity.ok(kakaoPayService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카카오 결제정보 삭제", description = "카카오페이 결제정보 삭제")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kakaoPayService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

