package ru.skillbox.group39.socialnetwork.notifications.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.AccountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.Count;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationCountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationStampedDto;
import ru.skillbox.group39.socialnetwork.notifications.model.AuthorModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationCommonModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationSimpleModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationStampedModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.AuthorRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.skillbox.group39.socialnetwork.notifications.utils.TimestampUtils.NOW;

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
	private final AuthorRepository authorRepository;
	private final UsersClient usersClient;


	@Override
	public Object getCount(Pageable pageable) {
		Count count = new Count(notificationStampedRepository.findAll(pageable).getTotalElements());
		return new NotificationCountDto(LocalDateTime.now(), count);
	}

	@Override
	@Transactional
	public void processCommonModel(NotificationCommonModel notificationCommonModel) {
		notificationCommonRepository.save(notificationCommonModel);
		log.info(" * NotificationCommonModel saved to DB/notifications/notifications_common");

		processNativeModels(notificationCommonModel);
	}

	@Override
	@Transactional
	public void processNativeModels(@NotNull NotificationCommonModel model) {

		NotificationSimpleModel notificationSimpleModel = createSimpleNotification(model);

		log.info(" * Wrap simple model to stamped model");
		NotificationStampedModel notificationStampedModel = new NotificationStampedModel(notificationSimpleModel);

		try {
			notificationStampedRepository.save(notificationStampedModel);
			log.info(" * AuthorModel saved to DB/notifications/author_of_notification");
			log.info(" * NotificationSimpleModel saved to DB/notifications/notifications_simple");
			log.info(" * NotificationStampedModel saved to DB/notifications/notifications_stamped");
		} catch (RuntimeException e) {
			log.error(" ! Exception during persisting notification models");
		}
	}

	@NotNull
	private NotificationSimpleModel createSimpleNotification(@NotNull NotificationCommonModel notificationCommonModel) {
		log.info(" * Transfer fields from NotificationCommonModel to NotificationSimpleModel");
		NotificationSimpleModel notificationSimpleModel = new NotificationSimpleModel();

		notificationSimpleModel.setAuthorId(notificationCommonModel.getFromUserId());
		notificationSimpleModel.setContent(notificationCommonModel.getText());
		notificationSimpleModel.setTimestamp(notificationCommonModel.getTimestamp());
		notificationSimpleModel.setNotificationType(notificationCommonModel.getType());

		AccountDto accountDto = usersClient.getUserDetailsById(notificationSimpleModel.getAuthorId());

//		TODO
//		Нужна ли обработка на null полей от юзера

		AuthorModel authorModel = authorRepository.findById(
				accountDto.getId()).orElseThrow();
		AuthorModel.builder()
				.id(accountDto.getId())
				.firstName(accountDto.getFirstName())
				.lastName(accountDto.getLastName())
				.build();

		notificationSimpleModel.setAuthor(authorModel);
		return notificationSimpleModel;
	}


	/**
	 * @Example <p>
	 * Right response to FE
	 * @Code [<p>
	 * &nbsp {<p>
	 * &nbsp&nbsp&nbsp"timeStamp": "2023-09-13T20:32:02.596+00:00",<p>
	 * &nbsp&nbsp&nbsp"data": {<p>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp"author": {<p>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"firstName": "Artem",<p>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"lastName": "Lebedev",<p>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"photo": ""<p>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp},<p>
	 * &nbsp&nbsp&nbsp"id": 1,<p>
	 * &nbsp&nbsp&nbsp"notificationType": "POST",<p>
	 * &nbsp&nbsp&nbsp"timestamp": "2023-09-13T19:47:04.538+00:00",<p>
	 * &nbsp&nbsp&nbsp"content": "Пользователь 7 опубликовал пост: 'JPA vs LiquiBase. Я создал changeset c OnetoOne'"<p>
	 * &nbsp }<p>
	 * ],<p>
	 */

	@Override
	public Object getPageSimpleNotifications(Pageable pageable) {
		log.info(" * service/NotificationServiceImpl/getPageNotifications");
		Page<NotificationSimpleModel> page = notificationSimpleRepository.findAll(pageable);

		List<NotificationDto> notificationDtoList = ObjectMapperUtils.mapAll(notificationSimpleRepository.findAll(), NotificationDto.class);

		List<NotificationStampedDto> notificationStampedDtoList = new ArrayList<>();
		notificationDtoList.forEach(dto -> {
			dto.setAuthor(new Author("Artem", "Lebedev", ""));
			NotificationStampedDto notificationStampedDto = new NotificationStampedDto(new Timestamp(System.currentTimeMillis()), dto);
			notificationStampedDtoList.add(notificationStampedDto);
		});

		return new ResponseEntity<>(notificationStampedDtoList, HttpStatus.OK);
	}


	@Override
	public Object getPageStampedNotifications(Pageable pageable) {
		log.info(" * service/NotificationServiceImpl/getPageNotificationStamped");
		Page<NotificationStampedModel> page = notificationStampedRepository.findAll(pageable);

//		List<NotificationStampedDto> notificationStampedDtoList = ObjectMapperUtils.mapAll(notificationStampedModelList, NotificationStampedDto.class);
//		for (NotificationStampedDto dto : notificationStampedDtoList){
//			dto.getData().setAuthorModel(new AuthorModel("Artem", "Lebedev", "/storage/bucket/img.png"));
//		}
//
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	/**
	 * @deprecated
	 */
	@Override
	public NotificationStampedDto getAllNotifications() {
		log.info(" * service/NotificationServiceImpl/getAllNotifications");

		NotificationStampedDto notificationStampedDto = new NotificationStampedDto();

		List<NotificationDto> notificationDtoList = ObjectMapperUtils.mapAll(notificationStampedRepository.findAll(), NotificationDto.class);
		return notificationStampedDto;
	}


	@Override
	public Object addEvent(@NotNull EventNotificationDto eventNotificationDto) {

		AccountDto accountDto = usersClient.getUserDetailsById(eventNotificationDto.getAuthorId());

		AuthorModel authorModel = authorRepository.findById(
				accountDto.getId()).orElseThrow();
				AuthorModel.builder()
						.id(accountDto.getId())
						.firstName(accountDto.getFirstName())
						.lastName(accountDto.getLastName())
						.build();

		NotificationSimpleModel notificationSimpleModel = NotificationSimpleModel.builder()
				.authorId(authorModel.getId())
				.content(eventNotificationDto.getContent())
				.notificationType(eventNotificationDto.getNotificationType().name())
				.timestamp(NOW())
				.author(authorModel)
				.build();

		NotificationStampedModel notificationStampedModel = new NotificationStampedModel(notificationSimpleModel);

		try {
			notificationStampedRepository.save(notificationStampedModel);
			log.info(" * NotificationSimpleModel saved to DB/notifications/notifications_simple");
			log.info(" * NotificationStampedModel saved to DB/notifications/notifications_stamped");
		} catch (RuntimeException e) {
			log.error(" ! Exception during persisting notification models");
		}

		return null;
	}
}
