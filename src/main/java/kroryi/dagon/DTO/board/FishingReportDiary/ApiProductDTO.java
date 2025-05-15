package kroryi.dagon.DTO.board.FishingReportDiary;

import kroryi.dagon.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiProductDTO {
    private Long prodId;
    private String prodName;

    public ApiProductDTO(Product product) {
        this.prodId = product.getProdId();
        this.prodName = product.getProdName();
    }
}
