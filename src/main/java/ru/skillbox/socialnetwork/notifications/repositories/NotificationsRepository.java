package ru.skillbox.socialnetwork.notifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.socialnetwork.notifications.entities.Notification;


public interface NotificationsRepository extends JpaRepository<Notification, Integer> {
	Notification findByAuthorId(String authorId);
}
