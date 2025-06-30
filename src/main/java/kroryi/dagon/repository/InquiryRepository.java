package kroryi.dagon.repository;

import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.ReceiverType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
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

    // inquiryType으로 검색
    Page<Inquiry> findByInquiryType(InquiryType inquiryType, Pageable pageable);

    // receiverType과 inquiryType으로 검색
    Page<Inquiry> findByReceiverTypeAndInquiryType(ReceiverType receiverType, InquiryType inquiryType, Pageable pageable);

    // inquiryType과 키워드로 검색
    @Query("SELECT i FROM Inquiry i " +
            "WHERE i.inquiryType = :inquiryType " +
            "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Inquiry> findByInquiryTypeAndKeyword(@Param("inquiryType") InquiryType inquiryType,
                                              @Param("keyword") String keyword,
                                              Pageable pageable);

    // receiverType, inquiryType, 키워드로 검색
    @Query("SELECT i FROM Inquiry i " +
            "WHERE i.receiverType = :receiverType " +
            "AND i.inquiryType = :inquiryType " +
            "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Inquiry> findByReceiverTypeAndInquiryTypeAndKeyword(@Param("receiverType") ReceiverType receiverType,
                                                             @Param("inquiryType") InquiryType inquiryType,
                                                             @Param("keyword") String keyword,
                                                             Pageable pageable);

    // inquiryType과 답변 상태로 검색
    Page<Inquiry> findByInquiryTypeAndIsAnswered(InquiryType inquiryType, boolean isAnswered, Pageable pageable);

    // receiverType, inquiryType, 답변 상태로 검색
    Page<Inquiry> findByReceiverTypeAndInquiryTypeAndIsAnswered(ReceiverType receiverType, 
                                                                InquiryType inquiryType, 
                                                                boolean isAnswered, 
                                                                Pageable pageable);

    // inquiryType, 답변 상태, 키워드로 검색
    @Query("SELECT i FROM Inquiry i " +
            "WHERE i.inquiryType = :inquiryType " +
            "AND i.isAnswered = :isAnswered " +
            "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Inquiry> findByInquiryTypeAndIsAnsweredAndKeyword(@Param("inquiryType") InquiryType inquiryType,
                                                           @Param("isAnswered") boolean isAnswered,
                                                           @Param("keyword") String keyword,
                                                           Pageable pageable);

    // receiverType, inquiryType, 답변 상태, 키워드로 검색
    @Query("SELECT i FROM Inquiry i " +
            "WHERE i.receiverType = :receiverType " +
            "AND i.inquiryType = :inquiryType " +
            "AND i.isAnswered = :isAnswered " +
            "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Inquiry> findByReceiverTypeAndInquiryTypeAndIsAnsweredAndKeyword(@Param("receiverType") ReceiverType receiverType,
                                                                          @Param("inquiryType") InquiryType inquiryType,
                                                                          @Param("isAnswered") boolean isAnswered,
                                                                          @Param("keyword") String keyword,
                                                                          Pageable pageable);

    // 답변 상태별 필터링
    @Query("SELECT i FROM Inquiry i WHERE i.receiverType = :receiverType AND i.isAnswered = :isAnswered")
    Page<Inquiry> findByReceiverTypeAndIsAnswered(@Param("receiverType") ReceiverType receiverType,
                                                  @Param("isAnswered") boolean isAnswered,
                                                  Pageable pageable);

    // 답변 상태별 + 키워드 검색
    @Query("SELECT i FROM Inquiry i " +
            "WHERE i.receiverType = :receiverType " +
            "AND i.isAnswered = :isAnswered " +
            "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Inquiry> findByReceiverTypeAndIsAnsweredAndKeyword(@Param("receiverType") ReceiverType receiverType,
                                                            @Param("isAnswered") boolean isAnswered,
                                                            @Param("keyword") String keyword,
                                                            Pageable pageable);

    // 사용자-파트너 간 문의 조회
    List<Inquiry> findByUser_UnoAndPartner_Uno(Long userUno, Long partnerUno);

    // 파트너에게 온 문의 조회
    List<Inquiry> findByPartner_Uno(Long partnerUno);

    // 사용자별 문의 조회
    List<Inquiry> findByUser_Uno(Long userUno);

    // 사용자별 inquiryType 문의 조회
    List<Inquiry> findByUser_UnoAndInquiryType(Long userUno, InquiryType inquiryType);

    // 파트너별 inquiryType 문의 조회
    List<Inquiry> findByPartner_UnoAndInquiryType(Long partnerUno, InquiryType inquiryType);

    // 사용자별 문의 개수
    Long countByUser_Uno(Long userUno);

    // 파트너별 미답변 문의 개수
    Long countByPartner_UnoAndIsAnsweredFalse(Long partnerUno);

    // 파트너별 답변된 문의 개수
    Long countByPartner_UnoAndIsAnsweredTrue(Long partnerUno);

    // 파트너별 inquiryType 미답변 문의 개수
    Long countByPartner_UnoAndInquiryTypeAndIsAnsweredFalse(Long partnerUno, InquiryType inquiryType);

    // 파트너별 inquiryType 답변된 문의 개수
    Long countByPartner_UnoAndInquiryTypeAndIsAnsweredTrue(Long partnerUno, InquiryType inquiryType);

    // 최근 문의 10개 조회
    List<Inquiry> findTop10ByOrderByCreatedAtDesc();

    // inquiryType별 최근 문의 조회
    List<Inquiry> findTop10ByInquiryTypeOrderByCreatedAtDesc(InquiryType inquiryType);

    // 특정 기간 내 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.createdAt BETWEEN :startDate AND :endDate")
    List<Inquiry> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    // 특정 기간 내 inquiryType 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.inquiryType = :inquiryType AND i.createdAt BETWEEN :startDate AND :endDate")
    List<Inquiry> findByInquiryTypeAndCreatedAtBetween(@Param("inquiryType") InquiryType inquiryType,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    // 특정 기간 내 파트너별 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.partner.uno = :partnerUno AND i.createdAt BETWEEN :startDate AND :endDate")
    List<Inquiry> findByPartner_UnoAndCreatedAtBetween(@Param("partnerUno") Long partnerUno,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    // 특정 기간 내 파트너별 inquiryType 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.partner.uno = :partnerUno AND i.inquiryType = :inquiryType AND i.createdAt BETWEEN :startDate AND :endDate")
    List<Inquiry> findByPartner_UnoAndInquiryTypeAndCreatedAtBetween(@Param("partnerUno") Long partnerUno,
                                                                   @Param("inquiryType") InquiryType inquiryType,
                                                                   @Param("startDate") LocalDateTime startDate,
                                                                   @Param("endDate") LocalDateTime endDate);

    // 특정 기간 내 사용자별 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.user.uno = :userUno AND i.createdAt BETWEEN :startDate AND :endDate")
    List<Inquiry> findByUser_UnoAndCreatedAtBetween(@Param("userUno") Long userUno,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    // 특정 기간 내 사용자별 inquiryType 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.user.uno = :userUno AND i.inquiryType = :inquiryType AND i.createdAt BETWEEN :startDate AND :endDate")
    List<Inquiry> findByUser_UnoAndInquiryTypeAndCreatedAtBetween(@Param("userUno") Long userUno,
                                                                @Param("inquiryType") InquiryType inquiryType,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);

    // 문의 유형별 개수 조회
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.inquiryType = :inquiryType")
    Long countByInquiryType(@Param("inquiryType") InquiryType inquiryType);

    // 파트너별 문의 유형별 개수 조회
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.partner.uno = :partnerUno AND i.inquiryType = :inquiryType")
    Long countByPartner_UnoAndInquiryType(@Param("partnerUno") Long partnerUno,
                                         @Param("inquiryType") InquiryType inquiryType);

    // 사용자별 문의 유형별 개수 조회
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.user.uno = :userUno AND i.inquiryType = :inquiryType")
    Long countByUser_UnoAndInquiryType(@Param("userUno") Long userUno,
                                      @Param("inquiryType") InquiryType inquiryType);

    // 답변 대기 시간이 긴 문의 조회 (7일 이상)
    @Query("SELECT i FROM Inquiry i WHERE i.isAnswered = false AND i.createdAt < :sevenDaysAgo")
    List<Inquiry> findUnansweredInquiriesOlderThan(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    // inquiryType별 답변 대기 시간이 긴 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.inquiryType = :inquiryType AND i.isAnswered = false AND i.createdAt < :sevenDaysAgo")
    List<Inquiry> findUnansweredInquiriesByInquiryTypeOlderThan(@Param("inquiryType") InquiryType inquiryType,
                                                               @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    // 파트너별 답변 대기 시간이 긴 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.partner.uno = :partnerUno AND i.isAnswered = false AND i.createdAt < :sevenDaysAgo")
    List<Inquiry> findUnansweredInquiriesByPartnerOlderThan(@Param("partnerUno") Long partnerUno,
                                                           @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    // 파트너별 inquiryType 답변 대기 시간이 긴 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.partner.uno = :partnerUno AND i.inquiryType = :inquiryType AND i.isAnswered = false AND i.createdAt < :sevenDaysAgo")
    List<Inquiry> findUnansweredInquiriesByPartnerAndInquiryTypeOlderThan(@Param("partnerUno") Long partnerUno,
                                                                         @Param("inquiryType") InquiryType inquiryType,
                                                                         @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    // 사용자별 문의 삭제
    void deleteAllByUser_Uno(Long userUno);

    // 사용자별 inquiryType 문의 삭제
    void deleteAllByUser_UnoAndInquiryType(Long userUno, InquiryType inquiryType);

    // 파트너별 문의 삭제
    void deleteAllByPartner_Uno(Long partnerUno);

    // 파트너별 inquiryType 문의 삭제
    void deleteAllByPartner_UnoAndInquiryType(Long partnerUno, InquiryType inquiryType);

    // 특정 기간 내 문의 삭제
    @Query("DELETE FROM Inquiry i WHERE i.createdAt < :date")
    void deleteAllByCreatedAtBefore(@Param("date") LocalDateTime date);

    // inquiryType별 특정 기간 내 문의 삭제
    @Query("DELETE FROM Inquiry i WHERE i.inquiryType = :inquiryType AND i.createdAt < :date")
    void deleteAllByInquiryTypeAndCreatedAtBefore(@Param("inquiryType") InquiryType inquiryType,
                                                 @Param("date") LocalDateTime date);
}