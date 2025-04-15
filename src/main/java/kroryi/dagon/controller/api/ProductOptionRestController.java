package kroryi.dagon.controller.api;

import kroryi.dagon.DTO.ProductOptionDTO;
import kroryi.dagon.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/prod_option")
public class ProductOptionRestController {

    private final ProductOptionService prod_optionService;

    @GetMapping("/all")
    public List<ProductOptionDTO> getAllProductOptions() {
        return prod_optionService.getAllProductOptions();
    }

}
