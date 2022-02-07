package com.dlisaev.cropper.repository;
import com.dlisaev.cropper.entity.Notification;
import com.dlisaev.cropper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserToOrderByCreateDate(User user);
    List<Notification> findAllByUserFromOrderByCreateDate(User user);
    Optional<Notification> findNotificationByIdAndUserTo(Long id, User user);
    List<Notification> findAllByUserToAndHasReadOrderByCreateDate(User user, Boolean hasRead);
}
