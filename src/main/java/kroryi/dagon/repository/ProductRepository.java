package kroryi.dagon.repository;

import kroryi.dagon.entity.Product;
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


}
