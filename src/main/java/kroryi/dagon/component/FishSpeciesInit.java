package kroryi.dagon.component;

import kroryi.dagon.entity.ProductFishSpecies;
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
                    new ProductFishSpecies("가숭어", null),
                    new ProductFishSpecies("갈치(내만)", null),
                    new ProductFishSpecies("갈치(먼바다)", null),
                    new ProductFishSpecies("감성돔", null),
                    new ProductFishSpecies("갑오징어", null),
                    new ProductFishSpecies("고등어", null),
                    new ProductFishSpecies("광어", null),
                    new ProductFishSpecies("낙지", null),
                    new ProductFishSpecies("농어", null),
                    new ProductFishSpecies("능성어", null),
                    new ProductFishSpecies("대구", null),
                    new ProductFishSpecies("도다리", null),
                    new ProductFishSpecies("독가시치", null),
                    new ProductFishSpecies("돌돔", null),
                    new ProductFishSpecies("동갈돗돔", null),
                    new ProductFishSpecies("무늬오징어", null),
                    new ProductFishSpecies("무점매가리", null),
                    new ProductFishSpecies("문치가자미", null),
                    new ProductFishSpecies("민어", null),
                    new ProductFishSpecies("방어", null),
                    new ProductFishSpecies("백조기", null),
                    new ProductFishSpecies("벵에돔", null),
                    new ProductFishSpecies("벤자리", null),
                    new ProductFishSpecies("볼락", null),
                    new ProductFishSpecies("부시리", null),
                    new ProductFishSpecies("불볼락(열기)", null),
                    new ProductFishSpecies("붉바리", null),
                    new ProductFishSpecies("붉퉁돔", null),
                    new ProductFishSpecies("붕장어", null),
                    new ProductFishSpecies("살오징어", null),
                    new ProductFishSpecies("삼치", null),
                    new ProductFishSpecies("송어", null),
                    new ProductFishSpecies("숭어", null),
                    new ProductFishSpecies("쏨뱅이", null),
                    new ProductFishSpecies("옥돔", null),
                    new ProductFishSpecies("어름돔", null),
                    new ProductFishSpecies("용가자미", null),
                    new ProductFishSpecies("임연수어", null),
                    new ProductFishSpecies("자바리", null),
                    new ProductFishSpecies("전갱이", null),
                    new ProductFishSpecies("전어", null),
                    new ProductFishSpecies("조피볼락(우럭)", null),
                    new ProductFishSpecies("점성어", null),
                    new ProductFishSpecies("주꾸미", null),
                    new ProductFishSpecies("쥐치", null),
                    new ProductFishSpecies("참가자미", null),
                    new ProductFishSpecies("참돔", null),
                    new ProductFishSpecies("참문어", null),
                    new ProductFishSpecies("참우럭", null),
                    new ProductFishSpecies("큰민어", null),
                    new ProductFishSpecies("피문어", null),
                    new ProductFishSpecies("학공치", null),
                    new ProductFishSpecies("한치", null),
                    new ProductFishSpecies("호래기", null),
                    new ProductFishSpecies("황열기", null)
            ));
        }
    }
}
