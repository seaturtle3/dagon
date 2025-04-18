package kroryi.dagon.service;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.FishSpeciesRepository;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.ReservationRepository;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    public String getFindAll(){
        return reservationRepository.findAll().toString();
    }

    public List<ProductDTO> getAllProductsByRegionAndMainType(ProdRegion region, MainType mainType) {
        if (region == null || region.equals("전체")) {
            return productRepository.findByMainType(mainType)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        } else {
            return productRepository.findByProdRegionAndMainType(region, mainType)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        }
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdName(product.getProdName());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setMinPerson(product.getMinPerson());
        dto.setWeight(product.getWeight());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());
        return dto;
    }


}
