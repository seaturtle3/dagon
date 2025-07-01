package kroryi.dagon.service.product;

import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.product.ProductImage;
import kroryi.dagon.repository.product.ProductImageRepository;
import kroryi.dagon.service.image.ImageContentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductImageContentProvider implements ImageContentProvider {

    private final ProductImageRepository productImageRepository;

    @Override
    public String getBoardName() {
        return "상품 이미지";
    }

    @Override
    public List<String> getAllContents() {
        // 상품 이미지의 fileName들을 반환
        return productImageRepository.findAll().stream()
                .map(ProductImage::getFileName)
                .collect(Collectors.toList());
    }
} 