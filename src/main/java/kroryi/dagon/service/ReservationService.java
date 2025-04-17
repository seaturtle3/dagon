package kroryi.dagon.service;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.FishSpeciesRepository;
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
    private final ProductService productService;

    public String getFindAll(){
        return reservationRepository.findAll().toString();
    }

    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }



}
