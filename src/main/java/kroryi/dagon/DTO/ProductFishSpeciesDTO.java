package kroryi.dagon.DTO;

import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.MainType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class ProductFishSpeciesDTO {
    private Long fsId;
    private String fsName;
    private String fsIconUrl;
    private MainType mainType;

    public ProductFishSpeciesDTO(ProductFishSpecies entity) {
        this.fsId = entity.getFsId();
        this.fsName = entity.getFsName();
        this.fsIconUrl = entity.getFsIconUrl();
        this.mainType = entity.getMainType();
    }

}
