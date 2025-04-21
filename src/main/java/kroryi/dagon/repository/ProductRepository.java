package kroryi.dagon.repository;

import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProdRegion(ProdRegion region);
    List<Product> findByMainType(MainType mainType);
    List<Product> findByProdRegionAndMainType(ProdRegion region, MainType mainType);
}
