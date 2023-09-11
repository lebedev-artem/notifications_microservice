package ru.skillbox.socialnetwork.notifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.socialnetwork.notifications.model.CommonNotifyModel;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:24
 */
@Repository
public interface CommonNotifyRepository extends JpaRepository<CommonNotifyModel, Long> {
}