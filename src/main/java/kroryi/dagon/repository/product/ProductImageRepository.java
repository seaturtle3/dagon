package kroryi.dagon.repository.product;

import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteByProductAndFileName(Product product, String fileName);
}
