package kroryi.dagon.repository;

import kroryi.dagon.entity.FreeBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {
    List<FreeBoardComment> findByFreeBoard_FbId(Long fbId);
}