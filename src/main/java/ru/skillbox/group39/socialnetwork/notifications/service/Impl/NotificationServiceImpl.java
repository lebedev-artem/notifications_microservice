package ru.skillbox.group39.socialnetwork.notifications.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.AccountDto;
import ru.skillbox.group39.socialnetwork.notifications.config.ObjectMapperCustom;
import ru.skillbox.group39.socialnetwork.notifications.dto.Count;
import ru.skillbox.group39.socialnetwork.notifications.dto.common.NotificationCommonDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationCountDto;
import ru.skillbox.group39.socialnetwork.notifications.exception.ErrorResponse;
import ru.skillbox.group39.socialnetwork.notifications.model.AuthorModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationCommonModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationSimpleModel;
import ru.skillbox.group39.socialnetwork.notifications.model.NotificationStampedModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.AuthorRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationCommonRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationSimpleRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationStampedRepository;
import ru.skillbox.group39.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.group39.socialnetwork.notifications.client.UsersClient;
import ru.skillbox.group39.socialnetwork.notifications.utils.ObjectMapperUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static ru.skillbox.group39.socialnetwork.notifications.utils.TimestampUtils.NOW;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationCommonRepository notificationCommonRepository;
	private final NotificationStampedRepository notificationStampedRepository;
	private final AuthorRepository authorRepository;
	private final UsersClient usersClient;
	private final ObjectMapperCustom objectMapperCustom;
	private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();


	@Override
	public Object getCount(Pageable pageable) {
		Count count = new Count(notificationStampedRepository.findAll(pageable).getTotalElements());
		return new NotificationCountDto(LocalDateTime.now(), count);
	}

	@Override
	@Transactional
	public void processCommonNotification(NotificationCommonDto notificationCommonDto) {
		NotificationCommonModel notificationCommonModel = ObjectMapperUtils.map(notificationCommonDto, NotificationCommonModel.class);
		log.info(" * NotificationCommonDto mapped to NotificationCommonModel entity. {}", notificationCommonModel);

		notificationCommonRepository.save(notificationCommonModel);
		log.info(" * NotificationCommonModel saved to DB/notifications/notifications_common");

		processNativeModels(notificationCommonDto);
	}

	@Override
	@Transactional
	public void processNativeModels(@NotNull NotificationCommonDto notificationCommonDto) {
		NotificationSimpleModel notificationSimpleModel = getNotificationSimpleModel(notificationCommonDto);
		AuthorModel authorModel = getAuthorModel(notificationCommonDto.getProducerId());
		notificationSimpleModel.setAuthor(authorModel);

		log.info(" * Wrap NotificationSimpleModel to NotificationStampedModel");
		NotificationStampedModel notificationStampedModel = new NotificationStampedModel(notificationSimpleModel);

		saveStampedModel(notificationStampedModel);
	}

	private static NotificationSimpleModel getNotificationSimpleModel(@NotNull NotificationCommonDto notificationCommonDto) {
		log.info(" * NotificationCommonDto mapped to NotificationSimpleModel entity");
		return ObjectMapperUtils.map(notificationCommonDto, NotificationSimpleModel.class);
	}

	/**
	 * @param id id of any person's object: AuthorId, UserId, ProducerId, ConsumerId
	 * @return AuthorModel: firstName, lastName, photo;
	 */
	private AuthorModel getAuthorModel(Long id) {
		log.info(" * Trying get AccountDTO from users microservice via FeignClient");
		AccountDto accountDto = usersClient.getUserDetailsById(id);
		return authorRepository.findById(
				accountDto.getId()).orElse(AuthorModel.builder()
				.id(accountDto.getId())
				.firstName(accountDto.getFirstName())
				.lastName(accountDto.getLastName())
				.photo(accountDto.getPhoto())
				.build());
	}

	private void saveStampedModel(NotificationStampedModel notificationStampedModel) {
		try {
			notificationStampedRepository.save(notificationStampedModel);
			log.info(" * AuthorModel persist into DB/notifications/author_of_notification");
			log.info(" * NotificationSimpleModel saved into DB/notifications/notifications_simple");
			log.info(" * NotificationStampedModel saved into DB/notifications/notifications_stamped");
		} catch (RuntimeException e) {
			log.error(" ! Exception during persisting notification models. {}", e.getMessage());
		}
	}

	/**
	 * @Example <p>
	 * Right response to FE
	 * @Code <p>
	 * [<p>
	 * {<p>
	 * timeStamp": "2023-09-13T20:32:02.596+00:00",<p>
	 * "data": {<p>
	 * "author": {<p>
	 * "firstName": "Artem",<p>
	 * "lastName": "Lebedev",<p>
	 * "photo": ""<p>
	 * },<p>
	 * "id": 1,<p>
	 * "notificationType": "POST",<p>
	 * "timestamp": "2023-09-13T19:47:04.538+00:00",<p>
	 * "content": "Пользователь 7 опубликовал пост: 'JPA vs LiquiBase. Я создал changeset c OnetoOne'"<p>
	 * }<p>
	 * ],<p>
	 */

	@Override
	public Object getPageStampedNotifications(Pageable pageable) {
		log.info(" * service/NotificationServiceImpl/getPageNotificationStamped");

		Page<NotificationStampedModel> page = notificationStampedRepository.findAll(pageable);

		List<NotificationStampedModel> pageListToDel = page.getContent();

		notificationStampedRepository.deleteAll(pageListToDel);

		/*
		TODO
		Кк узнать какие уведомления уже показаны?
		 */
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@Override
	public Object addEvent(@NotNull EventNotificationDto eventNotificationDto) {

		if (checkEventNotificationDtoFields(eventNotificationDto)) {
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, "Check all fields. Any is null"), HttpStatus.BAD_REQUEST);
		}

		NotificationCommonDto notificationCommonDto = mapAnyToCommonNotificationDto(eventNotificationDto);
		NotificationSimpleModel notificationSimpleModel = getNotificationSimpleModel(notificationCommonDto);

		AuthorModel authorModel = getAuthorModel(eventNotificationDto.getProducerId());
		notificationSimpleModel.setAuthor(authorModel);

		NotificationStampedModel notificationStampedModel = new NotificationStampedModel(notificationSimpleModel);

		saveStampedModel(notificationStampedModel);

		return new ResponseEntity<>(notificationSimpleModel, HttpStatus.OK);
	}

	@NotNull
	private static Boolean checkEventNotificationDtoFields(@NotNull EventNotificationDto eventNotificationDto) {
		return Stream.of(eventNotificationDto.getProducerId(),
				eventNotificationDto.getConsumerId(),
				eventNotificationDto.getNotificationType(),
				eventNotificationDto.getContent()).anyMatch(Objects::isNull);
	}

	/**
	 * @param anyDto
	 * @return NotificationCommonDto
	 * @throws JsonProcessingException
	 */
	private <T> @NotNull NotificationCommonDto mapAnyToCommonNotificationDto(T anyDto) {
		try {
			return objectMapperCustom.objectMapper().readValue(objectWriter.writeValueAsString(anyDto), NotificationCommonDto.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getSetting(Long userId) {

		return null;
	}

	@Override
	public Object setAllRead() {
		try {
			log.info(" * Attempt to mark all notifications as read");
			notificationStampedRepository.deleteAll();
		} catch (RuntimeException e) {
			log.error(" ! Exception during marking notifications as read");
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, "Fault during marking notifications as read"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
