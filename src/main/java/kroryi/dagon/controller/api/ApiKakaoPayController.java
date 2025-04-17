package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.KakaoPayDTO;
import kroryi.dagon.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kakaopay")
@RequiredArgsConstructor
public class ApiKakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping
    public ResponseEntity<KakaoPayDTO> create(@RequestBody KakaoPayDTO dto) {
        return ResponseEntity.ok(kakaoPayService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KakaoPayDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(kakaoPayService.get(id));
    }

    @Operation(summary = "결제 전체 조회")
    @GetMapping
    public ResponseEntity<List<KakaoPayDTO>> readAll() {
        return ResponseEntity.ok(kakaoPayService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<KakaoPayDTO> update(@PathVariable Long id, @RequestBody KakaoPayDTO dto) {
        return ResponseEntity.ok(kakaoPayService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kakaoPayService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

