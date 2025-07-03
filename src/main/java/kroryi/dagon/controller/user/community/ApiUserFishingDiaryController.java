package kroryi.dagon.controller.user.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingCenter.ApiFishingDiaryDTO;
import kroryi.dagon.service.community.fishingCenter.ApiFishingDiaryService;
import kroryi.dagon.component.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Tag(name = "FishingDiary", description = "조행기 API (일반회원)")
@RequestMapping("/api/fishing-diary")
@Log4j2
public class ApiUserFishingDiaryController {

    private final ApiFishingDiaryService apiFishingDiaryService;

    @Operation(summary = "조행기 생성")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiFishingDiaryDTO createFishingDiary(
            @RequestPart("dto") ApiFishingDiaryDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return apiFishingDiaryService.createFishingDiary(dto, userDetails.getUno(), images);
    }

    @Operation(summary = "나의 조행기 목록 조회")
    @GetMapping("/mine")
    public List<ApiFishingDiaryDTO> getMyDiaries(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return apiFishingDiaryService.getMyDiaries(userDetails.getUno());
    }

    @Operation(summary = "조행기 모두 조회 (페이징)")
    @GetMapping("/get-all")
    public Page<ApiFishingDiaryDTO> getAllFishingDiary(
            @RequestParam(defaultValue = "0") int page,
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

    @Operation(summary = "조행기 수정")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long updateFishingDiary(
            @PathVariable Long id,
            @RequestPart("dto") ApiFishingDiaryDTO apiFishingDiaryDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("content: {}", apiFishingDiaryDTO);
        return apiFishingDiaryService.updateFishingDiary(id, apiFishingDiaryDTO, userDetails.getUno(), images);
    }

//    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Long updateFishingDiaryJson(
//            @PathVariable Long id,
//            @RequestBody ApiFishingDiaryDTO apiFishingDiaryDTO,
//            @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//        return apiFishingDiaryService.updateFishingDiary(id, apiFishingDiaryDTO, userDetails.getUno());
//    }

    @Operation(summary = "조행기 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingDiary(@PathVariable Long id) {
        apiFishingDiaryService.deleteFishingDiary(id);
    }

    @Operation(summary = "내가 등록한 상품의 모든 조행기 조회")
    @GetMapping("/my-products-diaries")
    public List<ApiFishingDiaryDTO> getDiariesByMyProducts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return apiFishingDiaryService.getDiariesByMyProducts(userDetails.getUno());
    }

}
