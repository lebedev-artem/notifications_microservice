package ru.skillbox.group39.socialnetwork.notifications.service;

import org.springframework.data.domain.Pageable;
import ru.skillbox.group39.socialnetwork.notifications.model.CommonNotifyModel;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

public interface NotificationService {

	void saveCommonNotify(CommonNotifyModel model);
	Object getAllNotifications();
	Object getPageNotifications(Pageable pageable);

}
