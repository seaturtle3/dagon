package kroryi.dagon.repository;

import kroryi.dagon.entity.Report;
import kroryi.dagon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByReportedUname(String uname, Pageable pageable);
    void deleteById(Long id);
    boolean existsByReporterAndReported(User reporter, User reported);
}
