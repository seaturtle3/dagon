package kroryi.dagon.repository;

import kroryi.dagon.entity.Notification;
import kroryi.dagon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findByReceiver_UnoOrderByCreatedAtDesc(Long uno);
    // uno -> uid로 변경 (receiver.uid 기준으로 조회)
    List<Notification> findByReceiver_UidOrderByCreatedAtDesc(String receiverUid);

    Page<Notification> findByReceiver_Uid(String receiverUid, Pageable pageable);

    Page<Notification> findByType(String type, Pageable pageable);

    Page<Notification> findByReceiver_UidAndType(String receiverUid, String type, Pageable pageable);

}