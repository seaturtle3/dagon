package kroryi.dagon.repository.product;

import kroryi.dagon.entity.product.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<Product> findByProdRegionAndMainType(ProdRegion region, MainType mainType);

    List<Product> findByPartner_Uno(Long uno);

    List<Product> findByPartner_UnoAndDeletedFalse(Long uno);

    // 필터링 : 메인타입, 서브타입, 지역 각각 상황에 맞게
    List<Product> findByMainType(MainType mainType);

    List<Product> findByMainTypeAndProdRegion(MainType mainType, ProdRegion prodRegion);

    List<Product> findByMainTypeAndSubType(MainType mainType, SubType subType);

    List<Product> findByMainTypeAndSubTypeAndProdRegion(MainType mainType, SubType subType, ProdRegion prodRegion);

    @Query("""
                select distinct p from Product p
                left join p.fishingReports fr
                left join p.fishingDiaries fd
                where (fr.content is not null and fr.content <> '') 
                   or (fd.content is not null and fd.content <> '')
            """)
    Page<Product> findAllWithNonEmptyReportOrDiary(Pageable pageable);

    Long countByPartner_UnoAndDeletedFalse(Long partnerId);

    // ProductTest
    List<Product> findTop80ByOrderByProdIdAsc(); // 또는 정렬 없이 findAll(PageRequest.of(0, 80))

    //  -------------- 프론트 추가(바다/민물 필터) api ----------------
    Page<Product> findByMainTypeAndDeletedFalse(MainType mainType, Pageable pageable);

    //  -------------- 프론트 api 상단 필터 선택 시 제품 결과 ----------------
    @Query("""
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.fishSpeciesMappings m
            LEFT JOIN FETCH m.fs fs
            WHERE p.mainType = kroryi.dagon.enums.MainType.SEA
            AND (:region IS NULL OR p.prodRegion = :region)
            AND (:subType IS NULL OR p.subType = :subType)
            AND (:species IS NULL OR fs.fsName = :species)
            """)
    List<Product> findSeaProductsByFilters(
            @Param("region") ProdRegion region,
            @Param("subType") SubType subType,
            @Param("species") String species
    );

    // 프론트 SEA 상단 필터 제어
    // 한글로 지역, 세부 장소 받기
    @Query("SELECT DISTINCT p.prodRegion FROM Product p WHERE p.mainType = 'SEA'")
    List<ProdRegion> findDistinctRegions();

    @Query("SELECT DISTINCT p.subType FROM Product p WHERE p.mainType = 'SEA'")
    List<SubType> findDistinctSubTypes();

    @Query("""
                SELECT fs.fsName
                FROM ProductFishSpecies fs
                WHERE fs.mainType = kroryi.dagon.enums.MainType.SEA
            """)
    List<String> findAllSeaFishSpecies();

}
