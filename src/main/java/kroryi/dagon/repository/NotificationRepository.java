package kroryi.dagon.repository;

import kroryi.dagon.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


// 알람
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiver_UnoOrderByCreatedAtDesc(Long receiverUno);

    Page<Notification> findByReceiverUno(Long receiverUno, Pageable pageable); // uno 사용

    Page<Notification> findByType(String type, Pageable pageable);

    Page<Notification> findByReceiverUnoAndType(Long receiverUno, String type, Pageable pageable);
}