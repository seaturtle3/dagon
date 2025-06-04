package kroryi.dagon.repository;

import kroryi.dagon.entity.Report;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByReporter_UnameContaining(String uname, Pageable pageable);

    Page<Report> findByReported_UnameContaining(String uname, Pageable pageable);

    void deleteById(Long id);
    boolean existsByReporterAndReported(User reporter, User reported);


}
