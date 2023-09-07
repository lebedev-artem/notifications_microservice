package ru.skillbox.socialnetwork.notifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.socialnetwork.notifications.model.NotificationModel;


@Repository
public interface NotificationsRepository extends JpaRepository<NotificationModel, Integer> {
	NotificationModel findByAuthorId(Integer authorId);
}
