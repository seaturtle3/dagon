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
    public void run(String... args) throws Exception {
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
                    new ProductFishSpecies("벤자리", null)
            ));
        }
    }
}
