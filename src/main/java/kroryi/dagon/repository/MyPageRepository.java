package kroryi.dagon.repository;

import kroryi.dagon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPageRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);


    Optional<User> findByUno(Long uno);


    Optional<User> findByUname(String uname);}
