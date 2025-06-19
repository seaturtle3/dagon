package kroryi.dagon.DTO.product;

import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.MainType;
import lombok.Getter;
import lombok.Setter;

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
