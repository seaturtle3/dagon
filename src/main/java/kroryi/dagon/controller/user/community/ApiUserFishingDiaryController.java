package kroryi.dagon.controller.user.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.board.FishingCenter.ApiFishingDiaryDTO;
import kroryi.dagon.service.community.fishingCenter.ApiFishingDiaryService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "FishingDiary", description = "조행기 API (일반회원)")
@RequestMapping("/api/fishing-diary")
public class ApiUserFishingDiaryController {

    private final ApiFishingDiaryService apiFishingDiaryService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "조행기 생성")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiFishingDiaryDTO createFishingDiary(
            @RequestHeader("Authorization") String token,
            @RequestPart("fishingDiary") ApiFishingDiaryDTO apiFishingDiaryDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        String bearerToken = token.substring(7); // "Bearer " 제거
        Long userUno = jwtUtil.getUnoFromToken(bearerToken);
        return apiFishingDiaryService.createFishingDiary(apiFishingDiaryDTO, userUno, images);
    }

    @Operation(summary = "나의 조행기 목록 조회")
    @GetMapping("/mine")
    public List<ApiFishingDiaryDTO> getMyDiaries(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);
        return apiFishingDiaryService.getMyDiaries(uno);
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
    @PutMapping("/update/{id}")
    public Long updateFishingDiary(@PathVariable Long id,
                                   @RequestBody ApiFishingDiaryDTO apiFishingDiaryDTO) {
        return apiFishingDiaryService.updateFishingDiary(id, apiFishingDiaryDTO);
    }

    @Operation(summary = "조행기 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingDiary(@PathVariable Long id) {
        apiFishingDiaryService.deleteFishingDiary(id);
    }

}
