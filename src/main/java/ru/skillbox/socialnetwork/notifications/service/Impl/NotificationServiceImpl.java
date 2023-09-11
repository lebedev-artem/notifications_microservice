package ru.skillbox.socialnetwork.notifications.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.notifications.dto.notify.NotificationDto;
import ru.skillbox.socialnetwork.notifications.dto.notify.NotificationsDto;
import ru.skillbox.socialnetwork.notifications.model.CommonNotifyModel;
import ru.skillbox.socialnetwork.notifications.model.NotificationModel;
import ru.skillbox.socialnetwork.notifications.repositories.CommonNotifyRepository;
import ru.skillbox.socialnetwork.notifications.repositories.NotificationsRepository;
import ru.skillbox.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.socialnetwork.notifications.utils.ObjectMapperUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final CommonNotifyRepository commonNotifyRepository;
	private final NotificationsRepository notificationsRepository;

	@Override
	@Transactional
	public void saveCommonNotify(CommonNotifyModel commonNotifyModel) {
		commonNotifyRepository.save(commonNotifyModel);

		NotificationModel notificationModel = new NotificationModel();
		replicateToNotificationModel(notificationModel, commonNotifyModel);

		log.info(" * CommonNotifyModel saved to DB/notifications/commonnotifications");
	}

	private void replicateToNotificationModel(@NotNull NotificationModel notificationModel, @NotNull CommonNotifyModel commonNotifyModel) {
		notificationModel.setAuthorId(commonNotifyModel.getFromUserId());
		notificationModel.setContent(commonNotifyModel.getText());
		notificationModel.setSentTime(commonNotifyModel.getTimestamp());
		notificationsRepository.save(notificationModel);
		log.info(" * NotificationModel saved to DB/notifications/notifications");
	}

	@Override
	public NotificationsDto getAllNotifications() {
		NotificationsDto notificationsDto = new NotificationsDto();

		log.info(" * service/NotificationServiceImpl/getAllNotifications");
		List<NotificationDto> notificationDtoList = ObjectMapperUtils.mapAll(notificationsRepository.findAll(), NotificationDto.class);
		notificationsDto.setData(notificationDtoList);
		notificationsDto.setTimeStamp(new Timestamp(System.currentTimeMillis()));

		return notificationsDto;
	}
}
