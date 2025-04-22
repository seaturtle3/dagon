package kroryi.dagon.repository;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);
    boolean existsByUid(String uid);
    boolean existsByemail(String email);
    Optional<User> findByPhoneAndUname(String phone, String uname);
    Optional<User> findByEmail(String email);


    Page<User> findByUnameContainingOrEmailContaining(String search, String search1, Pageable pageable);

    Optional<User> findByUno(Long uno);
}
