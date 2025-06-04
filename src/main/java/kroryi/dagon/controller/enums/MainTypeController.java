package kroryi.dagon.controller.enums;

import kroryi.dagon.enums.MainType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enums/main-types")
public class MainTypeController {

    @GetMapping
    public List<Map<String, String>> getMainTypes() {
        return Arrays.stream(MainType.values())
                .map(type -> Map.of(
                        "name", type.name(),          // SEA, FRESHWATER
                        "korean", type.getKorean()    // 바다낚시, 민물낚시
                ))
                .toList();
    }
}
