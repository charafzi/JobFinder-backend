package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findNotificationByUserIdOrderByDateEnvoiDesc(Long userId, Pageable pageable);
}
