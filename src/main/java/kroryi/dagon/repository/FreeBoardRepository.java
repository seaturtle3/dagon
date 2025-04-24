package kroryi.dagon.repository;


import kroryi.dagon.entity.FreeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {
}