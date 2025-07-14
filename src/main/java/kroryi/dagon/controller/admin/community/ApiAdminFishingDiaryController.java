package kroryi.dagon.controller.admin.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingCenter.ApiFishingDiaryDTO;
import kroryi.dagon.service.community.fishingCenter.ApiFishingDiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin FishingDiary", description = "관리자 조행기 관리 API")
@RequestMapping("/api/admin/fishing-diary")
@Log4j2
public class ApiAdminFishingDiaryController {

    private final ApiFishingDiaryService apiFishingDiaryService;

    @Operation(summary = "조행기 전체 조회 (페이징)")
    @GetMapping("/get-all")
    public Page<ApiFishingDiaryDTO> getAllFishingDiaries(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "fdId") String sortBy,
                                                         @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return apiFishingDiaryService.getAllFishingDiary(pageable);
    }

    @Operation(summary = "조행기 ID 조회")
    @GetMapping("/get/{id}")
    public ApiFishingDiaryDTO getFishingDiary(@PathVariable Long id) {
        return apiFishingDiaryService.getFishingDiaryById(id);
    }

    @Operation(summary = "조행기 수정용 조회")
    @GetMapping("/edit/{id}")
    public ApiFishingDiaryDTO getFishingDiaryEdit(@PathVariable Long id) {
        return apiFishingDiaryService.getFishingDiaryById(id);
    }

    @Operation(summary = "조행기 수정")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long updateFishingDiary(
            @PathVariable("id") Long fdId,
            @RequestPart("dto") ApiFishingDiaryDTO apiFishingDiaryDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        // 관리자용 조행기 수정 - userUno는 조행기 작성자의 uno를 사용
        Long userUno = apiFishingDiaryDTO.getUser() != null ? apiFishingDiaryDTO.getUser().getUno() : null;
        
        if (userUno == null) {
            throw new RuntimeException("조행기 작성자 정보가 없습니다.");
        }
        
        log.info("Update fishing diary - fdId: {}, userUno: {}, images count: {}", 
                fdId, userUno, images != null ? images.size() : 0);
        
        if (images != null && !images.isEmpty()) {
            return apiFishingDiaryService.updateFishingDiary(fdId, apiFishingDiaryDTO, userUno, images);
        } else {
            return apiFishingDiaryService.updateFishingDiary(fdId, apiFishingDiaryDTO, userUno);
        }
    }

    @Operation(summary = "조행기 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingDiary(@PathVariable Long id) {
        apiFishingDiaryService.deleteFishingDiary(id);
    }
} 