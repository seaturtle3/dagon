package kroryi.dagon.repository;

import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.ReceiverType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // ADMIN receiverType만 검색+페이징 (제목 또는 내용에 키워드 포함)
    @Query("SELECT i FROM Inquiry i " +
            "WHERE i.receiverType = :receiverType " +
            "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Inquiry> findByReceiverTypeAndKeyword(@Param("receiverType") ReceiverType receiverType,
                                               @Param("keyword") String keyword,
                                               Pageable pageable);

    // ADMIN receiverType만 페이징 조회 (키워드 없을 때)
    Page<Inquiry> findByReceiverType(ReceiverType receiverType, Pageable pageable);

    List<Inquiry> findByUser_UnoAndPartner_Uno(Long userUno, Long partnerUno);

    List<Inquiry> findByPartner_Uno(Long partnerUno);


    List<Inquiry> findByUser_Uno(Long userUno);


}