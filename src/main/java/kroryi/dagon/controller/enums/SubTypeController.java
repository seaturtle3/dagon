package kroryi.dagon.controller.enums;

import kroryi.dagon.enums.SubType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enums/sub-types")
public class SubTypeController {

    @GetMapping
    public List<Map<String, String>> getSubTypes() {
        return Arrays.stream(SubType.values())
                .map(type -> Map.of(
                        "name", type.name(),                     // ENUM 이름
                        "korean", type.getKorean(),             // 한글명
                        "mainType", type.getMainType().name()   // SEA or FRESHWATER
                ))
                .toList();
    }
}
