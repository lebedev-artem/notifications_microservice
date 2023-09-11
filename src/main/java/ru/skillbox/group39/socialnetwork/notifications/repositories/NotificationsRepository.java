package ru.skillbox.group39.socialnetwork.notifications.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationModel;


@Repository
public interface NotificationsRepository extends JpaRepository<NotificationModel, Long>, PagingAndSortingRepository<NotificationModel,Long> {

	NotificationModel findByAuthorId(Long authorId);

	@Query("select n from NotificationModel n")
	@NotNull Page<NotificationModel> findAll(@NotNull Pageable pageable);
}
