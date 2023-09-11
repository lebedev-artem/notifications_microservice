package ru.skillbox.socialnetwork.notifications.service;

import ru.skillbox.socialnetwork.notifications.dto.notify.NotificationsDto;
import ru.skillbox.socialnetwork.notifications.model.CommonNotifyModel;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

public interface NotificationService {

	void saveCommonNotify(CommonNotifyModel model);

//	GET /
	NotificationsDto getAllNotifications();
}
