package kroryi.dagon.component;

import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.repository.FishSpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FishSpeciesInit implements CommandLineRunner {

    private final FishSpeciesRepository repository;

    @Override
    public void run(String... args) {
        if(repository.count() == 0) {
            repository.saveAll(List.of(
                    new ProductFishSpecies("가숭어", MainType.SEA, null),
                    new ProductFishSpecies("갈치(내만)", MainType.SEA, null),
                    new ProductFishSpecies("갈치(먼바다)", MainType.SEA, null),
                    new ProductFishSpecies("감성돔", MainType.SEA, null),
                    new ProductFishSpecies("갑오징어", MainType.SEA, null),
                    new ProductFishSpecies("고등어", MainType.SEA, null),
                    new ProductFishSpecies("광어", MainType.SEA, null),
                    new ProductFishSpecies("낙지", MainType.SEA, null),
                    new ProductFishSpecies("농어", MainType.SEA, null),
                    new ProductFishSpecies("능성어", MainType.SEA, null),
                    new ProductFishSpecies("대구", MainType.SEA, null),
                    new ProductFishSpecies("도다리", MainType.SEA, null),
                    new ProductFishSpecies("독가시치", MainType.SEA, null),
                    new ProductFishSpecies("돌돔", MainType.SEA, null),
                    new ProductFishSpecies("동갈돗돔", MainType.SEA, null),
                    new ProductFishSpecies("무늬오징어", MainType.SEA, null),
                    new ProductFishSpecies("무점매가리", MainType.SEA, null),
                    new ProductFishSpecies("문치가자미", MainType.SEA, null),
                    new ProductFishSpecies("민어", MainType.SEA, null),
                    new ProductFishSpecies("방어", MainType.SEA, null),
                    new ProductFishSpecies("백조기", MainType.SEA, null),
                    new ProductFishSpecies("벵에돔", MainType.SEA, null),
                    new ProductFishSpecies("벤자리", MainType.SEA, null),
                    new ProductFishSpecies("볼락", MainType.SEA, null),
                    new ProductFishSpecies("부시리", MainType.SEA, null),
                    new ProductFishSpecies("불볼락(열기)", MainType.SEA, null),
                    new ProductFishSpecies("붉바리", MainType.SEA, null),
                    new ProductFishSpecies("붉퉁돔", MainType.SEA, null),
                    new ProductFishSpecies("붕장어", MainType.SEA, null),
                    new ProductFishSpecies("살오징어", MainType.SEA, null),
                    new ProductFishSpecies("삼치", MainType.SEA, null),
                    new ProductFishSpecies("숭어", MainType.SEA, null),
                    new ProductFishSpecies("쏨뱅이", MainType.SEA, null),
                    new ProductFishSpecies("옥돔", MainType.SEA, null),
                    new ProductFishSpecies("어름돔", MainType.SEA, null),
                    new ProductFishSpecies("용가자미", MainType.SEA, null),
                    new ProductFishSpecies("임연수어", MainType.SEA, null),
                    new ProductFishSpecies("자바리", MainType.SEA, null),
                    new ProductFishSpecies("전갱이", MainType.SEA, null),
                    new ProductFishSpecies("전어", MainType.SEA, null),
                    new ProductFishSpecies("조피볼락(우럭)", MainType.SEA, null),
                    new ProductFishSpecies("점성어", MainType.SEA, null),
                    new ProductFishSpecies("주꾸미", MainType.SEA, null),
                    new ProductFishSpecies("쥐치", MainType.SEA, null),
                    new ProductFishSpecies("참가자미", MainType.SEA, null),
                    new ProductFishSpecies("참돔", MainType.SEA, null),
                    new ProductFishSpecies("참문어", MainType.SEA, null),
                    new ProductFishSpecies("참우럭", MainType.SEA, null),
                    new ProductFishSpecies("큰민어", MainType.SEA, null),
                    new ProductFishSpecies("피문어", MainType.SEA, null),
                    new ProductFishSpecies("학공치", MainType.SEA, null),
                    new ProductFishSpecies("한치", MainType.SEA, null),
                    new ProductFishSpecies("호래기", MainType.SEA, null),
                    new ProductFishSpecies("황열기", MainType.SEA, null)
            ));

            // 민물 낚시 고기 목록
            repository.saveAll(List.of(
                    new ProductFishSpecies("송어", MainType.FRESHWATER, null),
                    new ProductFishSpecies("가물치", MainType.FRESHWATER, null),
                    new ProductFishSpecies("가시고기", MainType.FRESHWATER, null),
                    new ProductFishSpecies("강준치", MainType.FRESHWATER, null),
                    new ProductFishSpecies("메기", MainType.FRESHWATER, null),
                    new ProductFishSpecies("쏘가리", MainType.FRESHWATER, null),
                    new ProductFishSpecies("배스", MainType.FRESHWATER, null)
            ));
        }
    }
}
