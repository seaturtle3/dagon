package kroryi.dagon.repository;

import kroryi.dagon.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // 신고당한 유저의 닉네임으로 조회 (페이징 처리)
    Page<Report> findByReportedUname(String uname, Pageable pageable);
}
