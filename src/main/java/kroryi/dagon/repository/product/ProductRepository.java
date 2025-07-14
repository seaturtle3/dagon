package kroryi.dagon.repository.product;

import kroryi.dagon.entity.product.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    //  -------------- 프론트 api (바다/민물 필터) ----------------
    Page<Product> findByMainTypeAndDeletedFalse(MainType mainType, Pageable pageable);

    //  -------------- 프론트 api 상단 필터 > 바다/민물 상품 가져오기 ----------------
    @Query("""
            SELECT DISTINCT p FROM Product p
            WHERE p.mainType = kroryi.dagon.enums.MainType.SEA
            AND (:region IS NULL OR p.prodRegion = :region)
            AND (:subType IS NULL OR p.subType = :subType)
            """)
    Page<Product> findSeaProductsByFilters(
            @Param("region") ProdRegion region,
            @Param("subType") SubType subType,
            @Param("species") List<String> species,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT p FROM Product p
            WHERE p.mainType = kroryi.dagon.enums.MainType.FRESHWATER
            AND (:region IS NULL OR p.prodRegion = :region)
            AND (:subType IS NULL OR p.subType = :subType)
            """)
    Page<Product> findFreshwaterProductsByFilters(
            @Param("region") ProdRegion region,
            @Param("subType") SubType subType,
            @Param("species") List<String> species,
            Pageable pageable
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

    // 프론트 어종 받아오기
    @Query("""
                SELECT fs.fsName
                FROM ProductFishSpecies fs
                WHERE fs.mainType = kroryi.dagon.enums.MainType.FRESHWATER
            """)
    List<String> findAllFreshwaterFishSpecies();

    // 키워드 검색 (상품명, 설명, 주소, 이벤트, 공지)
    @Query("""
        SELECT p FROM Product p
        WHERE (
            LOWER(p.prodName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR p.prodDescription LIKE CONCAT('%', :keyword, '%')
            OR LOWER(p.prodAddress) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR p.prodEvent LIKE CONCAT('%', :keyword, '%')
            OR p.prodNotice LIKE CONCAT('%', :keyword, '%')
        )
    """)
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<Product> findByProdName(String prodName);

}
