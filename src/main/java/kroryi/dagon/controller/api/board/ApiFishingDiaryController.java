package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingDiaryDTO;
import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.entity.FishingDiary;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.service.FishingDiaryService;
import kroryi.dagon.service.FishingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board-Community", description = "조행기 게시판 글쓰기/수정/삭제 API")
@RequestMapping("/api/fishing-diary")
public class ApiFishingDiaryController {

    private final FishingDiaryService fishingDiaryService;

    @Operation(summary = "조행기 생성")
    @PostMapping("/create")
    public FishingDiaryDTO createFishingDiary(@RequestBody FishingDiaryDTO fishingDiaryDTO) {
        return fishingDiaryService.createFishingDiary(fishingDiaryDTO);
    }

    @Operation(summary = "조행기 모두 조회")
    @GetMapping("/get-all")
    public List<FishingDiaryDTO> getAllFishingDiary() {
        return fishingDiaryService.getAllFishingDiary();
    }

    @Operation(summary = "조행기 ID 조회")
    @GetMapping("/get/{id}")
    public FishingDiary getFishingDiary(@PathVariable Long id) {
        return fishingDiaryService.getFishingDiary(id);
    }

    @Operation(summary = "조행기 수정")
    @PutMapping("/update/{id}")
    public Long updateFishingDiary(@PathVariable Long id,
                                   @RequestBody FishingDiaryDTO fishingDiaryDTO) {
        return fishingDiaryService.updateFishingDiary(id, fishingDiaryDTO);
    }

    @Operation(summary = "조행기 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingDiary(@PathVariable Long frId) {
        fishingDiaryService.deleteFishingDiary(frId);
    }

}
