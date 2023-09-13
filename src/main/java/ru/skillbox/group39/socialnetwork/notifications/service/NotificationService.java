package ru.skillbox.group39.socialnetwork.notifications.service;

import org.springframework.data.domain.Pageable;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationCommonModel;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

public interface NotificationService {

	void saveCommonNotify(NotificationCommonModel model);
	Object getAllNotifications();
	Object getPageNotifications(Pageable pageable);
	Object getPageNotificationStamped(Pageable pageable);

}
