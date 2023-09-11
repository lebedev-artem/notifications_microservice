package ru.skillbox.group39.socialnetwork.notifications.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationsDto;
import ru.skillbox.group39.socialnetwork.notifications.model.CommonNotifyModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.CommonNotifyRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationsRepository;
import ru.skillbox.group39.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.group39.socialnetwork.notifications.utils.ObjectMapperUtils;
import ru.skillbox.group39.socialnetwork.notifications.client.UsersClient;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.Author;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationDto;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
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
	private final UsersClient usersClient;

	@Override
	@Transactional
	public void saveCommonNotify(CommonNotifyModel commonNotifyModel) {
		commonNotifyRepository.save(commonNotifyModel);

		NotificationModel notificationModel = new NotificationModel();
		replicateToNotificationModel(notificationModel, commonNotifyModel);

		log.info(" * CommonNotifyModel saved to DB/notifications/commonnotifications");
	}

//	TODO
//	Нужно переписать на сохранение в базу NotificationsDto, и тогда можно Page сразу получить для них

	private void replicateToNotificationModel(@NotNull NotificationModel notificationModel, @NotNull CommonNotifyModel commonNotifyModel) {
		notificationModel.setAuthorId(commonNotifyModel.getFromUserId());
		notificationModel.setContent(commonNotifyModel.getText());
		notificationModel.setSentTime(commonNotifyModel.getTimestamp());
		notificationModel.setNotificationType(commonNotifyModel.getType());
		notificationsRepository.save(notificationModel);
		log.info(" * NotificationModel saved to DB/notifications/notifications");
	}

	@Override
	public NotificationsDto getAllNotifications() {
		NotificationsDto notificationsDto = new NotificationsDto();

		log.info(" * service/NotificationServiceImpl/getAllNotifications");
		List<NotificationDto> notificationDtoList = ObjectMapperUtils.mapAll(notificationsRepository.findAll(), NotificationDto.class);
		return notificationsDto;
	}

	@Override
	public Object getPageNotifications(Pageable pageable) {
		log.info(" * service/NotificationServiceImpl/getPageNotifications");
		Page<NotificationModel> page = notificationsRepository.findAll(pageable);
		List<NotificationModel> notificationModelList = page.getContent();

		List<NotificationDto> notificationDtoList = ObjectMapperUtils.mapAll(notificationsRepository.findAll(), NotificationDto.class);

//		TODO
//		Author
//			firstName
//			lastName
//		    photo
//
		List<NotificationsDto> notificationsDtoList = new ArrayList<>();
		for (NotificationDto dto : notificationDtoList){
			dto.setAuthor(new Author("Artem", "Lebedev", ""));
			NotificationsDto notificationsDto = new NotificationsDto(new Timestamp(System.currentTimeMillis()), dto);
			notificationsDtoList.add(notificationsDto);
		}

		return new ResponseEntity<>(notificationsDtoList, HttpStatus.OK);
	}
}
