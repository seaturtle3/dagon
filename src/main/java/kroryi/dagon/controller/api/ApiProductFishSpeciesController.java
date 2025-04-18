package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.repository.FishSpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product-fish-species")
public class ApiProductFishSpeciesController {

    private final FishSpeciesRepository fishSpeciesRepository;

    @Operation(summary = "모든 어종 리스트 조회", description = "저장된 모든 어종 정보를 조회합니다.")
    @GetMapping("/all")
    public List<ProductFishSpecies> getAllFishSpecies() {
        return fishSpeciesRepository.findAll();
    }

    @Operation(summary = "바다/민물 어종 리스트 조회", description = "바다/민물 구분")
    @GetMapping("/fishSpecies")
    public List<ProductFishSpecies> getFishSpecies(@RequestParam("type") MainType type) {
        return fishSpeciesRepository.findByMainType(type);
    }

}
