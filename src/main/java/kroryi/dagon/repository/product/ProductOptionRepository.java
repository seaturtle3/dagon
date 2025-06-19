package kroryi.dagon.repository.product;

import kroryi.dagon.entity.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption,Long> {
}
