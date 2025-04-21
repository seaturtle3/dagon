package kroryi.dagon.repository.board;

import kroryi.dagon.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByOrderByIsTopDescCreatedAtDesc(Pageable pageable);
}
