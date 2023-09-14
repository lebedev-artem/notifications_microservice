package ru.skillbox.group39.socialnetwork.notifications.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationCommonModel;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

public interface NotificationService {

	void processCommonModel(NotificationCommonModel model);
	void processNativeModels(@NotNull NotificationCommonModel model);

	Object getCount(Pageable pageable);
	Object getAllNotifications();
	Object getPageSimpleNotifications(Pageable pageable);
	Object getPageStampedNotifications(Pageable pageable);
	Object getSetting(Long userId);

	Object addEvent(EventNotificationDto eventNotificationDto);

}
