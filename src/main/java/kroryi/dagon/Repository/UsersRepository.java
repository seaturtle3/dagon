package kroryi.dagon.Repository;

import kroryi.dagon.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsById(String id);
}
