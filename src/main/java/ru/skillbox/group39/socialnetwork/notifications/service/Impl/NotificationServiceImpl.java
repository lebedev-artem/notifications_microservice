package ru.skillbox.group39.socialnetwork.notifications.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationStampedDto;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationCommonModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationSimpleModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationStampedModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationCommonRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationSimpleRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationStampedRepository;
import ru.skillbox.group39.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.group39.socialnetwork.notifications.utils.ObjectMapperUtils;
import ru.skillbox.group39.socialnetwork.notifications.client.UsersClient;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.Author;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationDto;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationCommonRepository notificationCommonRepository;
	private final NotificationSimpleRepository notificationSimpleRepository;
	private final NotificationStampedRepository notificationStampedRepository;
	private final UsersClient usersClient;

	@Override
	@Transactional
	public void saveCommonNotify(NotificationCommonModel notificationCommonModel) {

		notificationCommonRepository.save(notificationCommonModel);
		log.info(" * NotificationCommonModel saved to DB/notifications/notifications_common");

		NotificationSimpleModel notificationSimpleModel = replicateToNotificationModel(notificationCommonModel);

//		NotificationStampedModel notificationStampedModel = replicateToNotificationStampedModel(notificationSimpleModel);
	}

	@Transactional
	private @NotNull NotificationStampedModel replicateToNotificationStampedModel(NotificationSimpleModel notificationSimpleModel) {
		log.info(" * Creating NotificationStampedModel");
		Optional<NotificationSimpleModel> n = Optional.of(notificationSimpleRepository.findById(notificationSimpleModel.getId()).orElseThrow());
		NotificationStampedModel notificationStampedModel = new NotificationStampedModel();

		notificationStampedModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
		notificationStampedModel.setData(n.get());


		notificationStampedRepository.save(notificationStampedModel);
		log.info(" * NotificationStampedModel saved to DB/notifications/notifications_stamped");
		return notificationStampedModel;
	}

	@Transactional
	private @NotNull NotificationSimpleModel replicateToNotificationModel(@NotNull NotificationCommonModel notificationCommonModel) {
		log.info(" * Transfer fields from NotificationCommonModel to NotificationSimpleModel.");
		NotificationSimpleModel notificationSimpleModel = new NotificationSimpleModel();

		notificationSimpleModel.setAuthorId(notificationCommonModel.getFromUserId());
		notificationSimpleModel.setContent(notificationCommonModel.getText());
		notificationSimpleModel.setTimestamp(notificationCommonModel.getTimestamp());
		notificationSimpleModel.setNotificationType(notificationCommonModel.getType());



		NotificationStampedModel notificationStampedModel = new NotificationStampedModel(notificationSimpleModel);

//		notificationSimpleRepository.save(notificationSimpleModel);
//		log.info(" * NotificationSimpleModel saved to DB/notifications/notifications_simple");

		notificationStampedRepository.save(notificationStampedModel);
		log.info(" * NotificationStampedModel saved to DB/notifications/notifications_stamped");

		return notificationSimpleModel;
	}

	@Override
	public NotificationStampedDto getAllNotifications() {
		log.info(" * service/NotificationServiceImpl/getAllNotifications");

		NotificationStampedDto notificationStampedDto = new NotificationStampedDto();

		List<NotificationDto> notificationDtoList = ObjectMapperUtils.mapAll(notificationSimpleRepository.findAll(), NotificationDto.class);
		return notificationStampedDto;
	}

	@Override
	public Object getPageNotifications(Pageable pageable) {
		log.info(" * service/NotificationServiceImpl/getPageNotifications");
		Page<NotificationSimpleModel> page = notificationSimpleRepository.findAll(pageable);
		List<NotificationSimpleModel> notificationSimpleModelList = page.getContent();

		List<NotificationDto> notificationDtoList = ObjectMapperUtils.mapAll(notificationSimpleRepository.findAll(), NotificationDto.class);

//		TODO
//		Author
//			firstName
//			lastName
//		    photo
//
		List<NotificationStampedDto> notificationStampedDtoList = new ArrayList<>();
		for (NotificationDto dto : notificationDtoList){
			dto.setAuthor(new Author("Artem", "Lebedev", ""));
			NotificationStampedDto notificationStampedDto = new NotificationStampedDto(new Timestamp(System.currentTimeMillis()), dto);
			notificationStampedDtoList.add(notificationStampedDto);
		}

		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@Override
	public Object getPageNotificationStamped(Pageable pageable) {
		log.info(" * service/NotificationServiceImpl/getPageNotificationStamped");
		Page<NotificationStampedModel> page = notificationStampedRepository.findAll(pageable);
		List<NotificationStampedModel> notificationStampedModelList = page.getContent();
//
//		List<NotificationStampedDto> notificationStampedDtoList = ObjectMapperUtils.mapAll(notificationStampedModelList, NotificationStampedDto.class);
//		for (NotificationStampedDto dto : notificationStampedDtoList){
//			dto.getData().setAuthor(new Author("Artem", "Lebedev", "/storage/bucket/img.png"));
//		}
//
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
}
