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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
@Log4j2
public class ProductController {

    private final ProductService productService;
    private final PartnerService partnerService;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

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


    // 배 등록 페이지
    @GetMapping("/form")
    public String showRegisterForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("regions", regions());
        model.addAttribute("mainType", mainType());
        model.addAttribute("subType", subType());

        return "product/form";
    }

    // 배 등록 전송
    @PostMapping("/form")
    public String registerProduct(@ModelAttribute("product") Product product,
                                  @RequestParam("thumbnailFile") MultipartFile thumbnailFile) throws IOException {

        if (!thumbnailFile.isEmpty()) {
            String originalFilename = thumbnailFile.getOriginalFilename();
            String savedFileName = UUID.randomUUID() + "_" + originalFilename;

            // 저장 경로
            Path savePath = Paths.get(uploadDir, savedFileName);
            Files.copy(thumbnailFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            // 저장된 파일명을 DB에 저장
            product.setProdThumbnail(savedFileName);
        }

        // 파트너가 없으면 기본 파트너 설정
        if (product.getPartner() == null) {
            Partner defaultPartner = partnerService.getDefaultPartner();  // 기본 파트너 가져오기
            product.setPartner(defaultPartner);  // 상품에 파트너 자동 설정
        }

        productService.saveProduct(product); // 상품 저장
        return "redirect:/fishing-center"; // 등록 후 리스트 페이지로 리다이렉트
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
