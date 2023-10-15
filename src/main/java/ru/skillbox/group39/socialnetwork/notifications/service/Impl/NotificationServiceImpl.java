package ru.skillbox.group39.socialnetwork.notifications.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.group39.socialnetwork.notifications.client.FriendsClient;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.AccountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.ENotificationType;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationSimpleDto;
import ru.skillbox.group39.socialnetwork.notifications.exception.exceptions.UserPrincipalsNotFoundException;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationSimpleRepository;
import ru.skillbox.group39.socialnetwork.notifications.security.jwt.JwtUtils;
import ru.skillbox.group39.socialnetwork.notifications.utils.ObjectMapperCustom;
import ru.skillbox.group39.socialnetwork.notifications.dto.count.Count;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.common.NotificationCommonDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.count.NotificationsCountDto;
import ru.skillbox.group39.socialnetwork.notifications.exception.ErrorResponse;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.AuthorModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationCommonModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationSimpleModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationStampedModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.AuthorRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationCommonRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationStampedRepository;
import ru.skillbox.group39.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.group39.socialnetwork.notifications.client.UsersClient;
import ru.skillbox.group39.socialnetwork.notifications.utils.ObjectMapperUtils;

import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static ru.skillbox.group39.socialnetwork.notifications.security.service.UserDetailsServiceImpl.getPrincipalId;

/**
 * @author Artem Lebedev | 07/09/2023 - 08:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	@Value("${friends-service.host}")
	private String friendsHost;
	@Value("${friends-service.port}")
	private String friendsPort;

	private final NotificationCommonRepository notificationCommonRepository;
	private final NotificationStampedRepository notificationStampedRepository;
	private final NotificationSimpleRepository notificationSimpleRepository;
	private final AuthorRepository authorRepository;
	private final UsersClient usersClient;
	private final FriendsClient friendsClient;
	private final JwtUtils jwtUtils;

	@Override
	public Object getCount() {
		log.info(" * service/NotificationServiceImpl/getCount");
		Long cnt = notificationStampedRepository.countByData_ConsumerId(getPrincipalId());
		Count count = new Count(cnt);
		return new NotificationsCountDto(LocalDateTime.now(), count);
	}

	@Override
	public List<Long> getFriends(@NotNull AccountDto accountDto) {
		log.info(" * Getting list of friends for user id {}", accountDto.getId());
		String token = "";
		try {
			token = jwtUtils.getBearerToken(accountDto);
		}  catch (UserPrincipalNotFoundException e) {
			log.error(" ! Unable getting friends because token is null");
		}
		return friendsClient.getFriendsListById(token, accountDto.getId());
	}

	@Override
	public Optional<AccountDto> getUser(Long userId) {
		return Optional.ofNullable(usersClient.getUserDetailsById(userId));
	}

	@Override
	public void kafkaGoodBuy(NotificationCommonDto commonDTO) {
		processCommonNotification(commonDTO);
	}

	@Override
	@Transactional
	public void processCommonNotification(@NotNull NotificationCommonDto commonDTO) {
		log.info(" * service/NotificationServiceImpl/processCommonNotification");
		processCommonModel(commonDTO);
		if (commonDTO.getNotificationType() == null) {
			commonDTO.setNotificationType(ENotificationType.HANDY.name());
		}
		switch (commonDTO.getNotificationType()) {
			case "DO_LIKE":
			case "POST_COMMENT":
			case "FRIEND_REQUEST":
			case "HANDY":
				log.info(" * Start processing {}", commonDTO.getNotificationType());
				processNativeNotifications(getNotificationSimpleModel(commonDTO));
			case "POST":
			case "DEL_USER":
				log.info(" * Start processing {}", commonDTO.getNotificationType());
				Optional<AccountDto> userRequestingData = getUser(commonDTO.getProducerId());
				Optional<AuthorModel> aum = Optional.of(getAuthorModelFromId(commonDTO.getProducerId()));
				if (userRequestingData.isEmpty()) {
					return;
				}
				List<Long> friendsIdList = getFriends(userRequestingData.get());
				log.info(" * User id {} has {} friends", userRequestingData.get().getId(), friendsIdList.size());
				if (friendsIdList.isEmpty()) {
					return;
				}
				for (Long id : friendsIdList) {
					NotificationSimpleModel nsm = NotificationSimpleModel.builder()
							.producerId(commonDTO.getProducerId())
							.content(commonDTO.getContent())
							.notificationType(commonDTO.getNotificationType().toString())
							.timestamp(commonDTO.getTimestamp())
							.consumerId(id)
							.read(false)
							.author(aum.get())
							.build();

					log.info(" * Created simple notification for friend id {}", id);
					processNativeNotifications(nsm);
				}
		}
	}

	@Transactional
	private void processCommonModel(NotificationCommonDto notificationCommonDto) {
		NotificationCommonModel commonModel = ObjectMapperUtils.map(notificationCommonDto, NotificationCommonModel.class);
		log.info(" * NotificationCommonDto mapped to NotificationCommonModel entity. {}", commonModel);

		notificationCommonRepository.save(commonModel);
		log.info(" * NotificationCommonModel saved to DB/notifications/notifications_common. '{}'", commonModel);
	}

	@Transactional
	public void processNativeNotifications(@NotNull NotificationSimpleModel notificationSimpleModel) {
		log.info(" * Wrapping Notification_Simple_Model to Notification_Stamped_Model");
		NotificationStampedModel stampedModel = new NotificationStampedModel(notificationSimpleModel);
		try {
			authorRepository.save(stampedModel.getData().getAuthor());
			notificationSimpleRepository.save(stampedModel.getData());
			notificationStampedRepository.save(stampedModel);
			log.info(" * AuthorModel persist into DB/notifications/author. '{}'", stampedModel.getData().getAuthor());
			log.info(" * NotificationSimpleModel saved into DB/notifications/notifications_simple. '{}'", stampedModel.getData());
			log.info(" * NotificationStampedModel saved into DB/notifications/notifications_stamped. '{}'", stampedModel);
		} catch (RuntimeException e) {
			log.error(" ! Exception during persisting notification's models. {}", e.getMessage());
		}
	}

	private static NotificationSimpleModel getNotificationSimpleModel(@NotNull NotificationCommonDto notificationCommonDto) {
		log.info(" * Notification_Common_Dto mapped to Notification_Simple_Model entity");
		return ObjectMapperUtils.map(notificationCommonDto, NotificationSimpleModel.class);
	}

	/**
	 * @param id id of any person's object: AuthorId, UserId, ProducerId, ConsumerId
	 * @return AuthorModel: firstName, lastName, photo;
	 */
	public AccountDto getAccountPrincipals(@NotNull Long id) {
		log.info(" * service/NotificationServiceImpl/getAccountPrincipals");
		AccountDto accPrincipals;
		try {
			accPrincipals = usersClient.getUserDetailsById(id);
		} catch (RuntimeException e) {
			log.error(" ! User with id `{}` does not exist", id);
			throw new UserPrincipalsNotFoundException("User with id " + id + " does not exist");
		}
		return accPrincipals;
	}

	@NotNull
	public AuthorModel getAuthorModelFromId(Long id) {
		log.info(" * service/NotificationServiceImpl/getAuthorModelFromId");
		Optional<AuthorModel> aum = authorRepository.findById(id);
		if (aum.isPresent()) {
			return aum.get();
		} else {
			AccountDto acDto = getAccountPrincipals(id);
			return AuthorModel.builder()
					.id(acDto.getId())
					.firstName(acDto.getFirstName())
					.lastName(acDto.getLastName())
					.photo(acDto.getPhoto()).build();
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
		Long consumerId = getPrincipalId();

		Page<NotificationStampedModel> page = notificationStampedRepository.findAllByData_ConsumerId(consumerId, pageable);

		notificationStampedRepository.deleteAll(page);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@Override
	public Object addEvent(@NotNull EventNotificationDto eventNotificationDto) {
		log.info(" * service/NotificationServiceImpl/addEvent");
		if (checkEventNotificationDtoFields(eventNotificationDto)) {
			return new ResponseEntity<>(new ErrorResponse("Check all fields. Any is null", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}

		NotificationCommonDto commonDTO = ObjectMapperCustom.mapAnyToCommonNotificationDto(eventNotificationDto);
//		processCommonNotification(commonDTO);

		NotificationSimpleModel simpleModel = getNotificationSimpleModel(commonDTO);

		AuthorModel aum = getAuthorModelFromId(eventNotificationDto.getProducerId());
		simpleModel.setAuthor(aum);

		processNativeNotifications(simpleModel);
		return new ResponseEntity<>(ObjectMapperCustom.objectMapper().convertValue(simpleModel, NotificationSimpleDto.class), HttpStatus.OK);
	}

	@NotNull
	private static Boolean checkEventNotificationDtoFields(@NotNull EventNotificationDto eventNotificationDto) {
		log.info(" * service/NotificationServiceImpl/checkEventNotificationDtoFields");
		return Stream.of(eventNotificationDto.getProducerId(),
				eventNotificationDto.getConsumerId(),
				eventNotificationDto.getNotificationType(),
				eventNotificationDto.getContent()).anyMatch(Objects::isNull);
	}

	@Override
	public Object setAllRead() {
		log.info(" * service/NotificationServiceImpl/setAllRead");
		Long consumerId = getPrincipalId();
		try {
			log.info(" * Attempting to mark all notifications as read for user with id '{}'", consumerId);
			List<NotificationStampedModel> listToMarkAsRead = notificationStampedRepository.findAllByData_ConsumerId(consumerId);
			notificationStampedRepository.deleteAll(listToMarkAsRead);

		} catch (RuntimeException e) {
			log.error(" ! Exception during marking notifications as read");
			return new ResponseEntity<>(new ErrorResponse("Fault during marking notifications as read", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
