package ru.skillbox.group39.socialnetwork.notifications.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.AccountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationStampedDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.common.NotificationCommonDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

public interface NotificationService {

	void processCommonNotification(NotificationCommonDto notificationCommonDto);
	Object getCount();
	Long getCountBot();
	Object getPageStampedNotifications(Pageable pageable);
	List<NotificationStampedDto> getNotificationsForThisMan();
	Object setAllRead();
	Object addEvent(EventNotificationDto eventNotificationDto);
	void kafkaGoodBuy(NotificationCommonDto notificationCommonDto);
	Optional<AccountDto> getUser(Long userId);
	List<Long> getFriends(AccountDto accountDto);



}
