package kroryi.dagon.controller.enums;

import kroryi.dagon.enums.ProdRegion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enums/prod-regions")
public class ProdRegionController {

    @GetMapping
    public List<Map<String, String>> getRegions() {
        return Arrays.stream(ProdRegion.values())
                .map(region -> Map.of(
                        "name", region.name(),
                        "korean", region.getKorean()
                ))
                .toList();
    }
}
