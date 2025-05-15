package kroryi.dagon.controller;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.service.PartnerService;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
@Log4j2
public class ProductController {

    private final ProductService productService;
    private final PartnerService partnerService;

    private final ProductRepository productRepository;

    @ModelAttribute("regions")
    public List<ProdRegion> regions() {
        return Arrays.asList(ProdRegion.values());
    }

    @ModelAttribute("mainType")
    public List<MainType> mainType() {
        return Arrays.asList(MainType.values());
    }

    @ModelAttribute("subType")
    public List<SubType> subType() {
        return Arrays.asList(SubType.values());
    }


    // 배 등록 폼 페이지
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("regions", regions());
        model.addAttribute("mainType", mainType());
        model.addAttribute("subType", subType());

        return "product/form";
    }

    // 배 등록 처리
    @PostMapping("/register")
    public String registerProduct(@ModelAttribute("product") Product product) {
        // 파트너가 없으면 기본 파트너 설정
        if (product.getPartner() == null) {
            Partner defaultPartner = partnerService.getDefaultPartner();  // 기본 파트너 가져오기
            product.setPartner(defaultPartner);  // 상품에 파트너 자동 설정
        }
        log.info("등록받은 Product 데이터: {}", product);

        productService.saveProduct(product); // 상품 저장
        return "redirect:/product/list"; // 등록 후 리스트 페이지로 리다이렉트
    }

    // 배 리스트 페이지
    @GetMapping("/list")
    public String showProductList(Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("productList", products);

        log.info("List Get productList: {}", products);

        return "product/list"; // 리스트 페이지 반환
    }



}
