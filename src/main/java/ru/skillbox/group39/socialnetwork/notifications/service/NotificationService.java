package ru.skillbox.group39.socialnetwork.notifications.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import ru.skillbox.group39.socialnetwork.notifications.dto.common.NotificationCommonDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationCommonModel;

import java.security.Principal;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

public interface NotificationService {

	void processCommonNotification(NotificationCommonDto notificationCommonDto);
	void processNativeModels(@NotNull NotificationCommonDto notificationCommonDto);

	Object getCount();
	Object getPageStampedNotifications(Pageable pageable);
	Object getSetting(Long userId);
	Object setAllRead();

	Object addEvent(EventNotificationDto eventNotificationDto);

}
