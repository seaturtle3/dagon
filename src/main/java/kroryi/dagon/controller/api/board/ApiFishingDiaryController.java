package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingDiaryDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingDiaryDTO;
import kroryi.dagon.entity.FishingDiary;
import kroryi.dagon.service.board.fishingReportDiary.ApiFishingDiaryService;
import kroryi.dagon.service.board.fishingReportDiary.FishingDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board-Community", description = "조행기 게시판 글쓰기/수정/삭제 API")
@RequestMapping("/api/fishing-diary")
public class ApiFishingDiaryController {

    private final ApiFishingDiaryService apiFishingDiaryService;

    @Operation(summary = "조행기 생성")
    @PostMapping("/create")
    public ApiFishingDiaryDTO createFishingDiary(@RequestBody ApiFishingDiaryDTO apiFishingDiaryDTO) {
        return apiFishingDiaryService.createFishingDiary(apiFishingDiaryDTO);
    }

    @Operation(summary = "조행기 모두 조회")
    @GetMapping("/get-all")
    public List<ApiFishingDiaryDTO> getAllFishingDiary() {
        return apiFishingDiaryService.getAllFishingDiary();
    }

    @Operation(summary = "조행기 ID 조회")
    @GetMapping("/get/{id}")
    public FishingDiary getFishingDiary(@PathVariable Long id) {
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
