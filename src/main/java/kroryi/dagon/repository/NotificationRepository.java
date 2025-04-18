package kroryi.dagon.repository;

import kroryi.dagon.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


// 알람
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver_UnoOrderByCreatedAtDesc(Long receiverId);
}