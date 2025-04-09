package kroryi.dagon.repository;

import kroryi.dagon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);
    boolean existsByUid(String uid);
    boolean existsByemail(String email);


}
