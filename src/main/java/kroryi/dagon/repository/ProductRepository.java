package kroryi.dagon.repository;

import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByProdRegion(ProdRegion region);

    List<Product> findByMainType(MainType mainType);

    List<Product> findByProdRegionAndMainType(ProdRegion region, MainType mainType);

//    @Query("SELECT p FROM Product p WHERE "
//            + "(:region IS NULL OR p.prodRegion = :region) "
//            + "AND (:mainType IS NULL OR p.mainType = :mainType) "
//            + "AND (:subType IS NULL OR p.subType = :subType) "
//            + "AND (:fishType IS NULL OR p.fishType = :fishType) "
//            + "AND (:date IS NULL OR p.availableDate = :date) "
//            + "AND p.createdAt <= CURRENT_TIMESTAMP")
//    List<Product> searchProducts(
//            @Param("date") LocalDate date,
//            @Param("region") ProdRegion region,
//            @Param("mainType") MainType mainType,
//            @Param("subType") SubType subType,
//            @Param("fishType") String fishType
//    );

    @Query("SELECT p FROM Product p WHERE p.createdAt = :date AND p.prodRegion = :region AND p.mainType = :mainType AND p.subType = :subType AND p.prodName LIKE %:fishType%")
    List<Product> searchProducts(LocalDate date, ProdRegion region, MainType mainType, SubType subType, String fishType);


}
