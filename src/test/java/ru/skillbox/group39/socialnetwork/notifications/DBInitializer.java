package ru.skillbox.group39.socialnetwork.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.ENotificationType;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.common.EServiceName;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.AuthorModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationCommonModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationSimpleModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationStampedModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.AuthorRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationCommonRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationSimpleRepository;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationStampedRepository;

import java.sql.Timestamp;
import java.util.UUID;

import static ru.skillbox.group39.socialnetwork.notifications.utils.TimestampUtils.NOW;

/**
 * @author Artem Lebedev | 09/10/2023 - 14:51
 */
@RequiredArgsConstructor
@Component
public class DBInitializer {
	private final AuthorRepository authorRepository;
	private final NotificationSimpleRepository notificationSimpleRepository;
	private final NotificationStampedRepository notificationStampedRepository;
	private final NotificationCommonRepository notificationCommonRepository;

	public void setUp() {
		AuthorModel authorModel = AuthorModel.builder()
				.id(233L)
				.firstName("Artem")
				.lastName("Lebedev")
				.photo("photo url")
				.build();
		authorRepository.saveAndFlush(authorModel);
		AuthorModel authorModel2 = AuthorModel.builder()
				.id(235L)
				.firstName("Alexandr")
				.lastName("Eremeev")
				.photo("photo url")
				.build();
		authorRepository.saveAndFlush(authorModel2);

		NotificationSimpleModel notificationSimpleModel = NotificationSimpleModel.builder()
				.producerId(233L)
				.content("")
				.notificationType(ENotificationType.POST.name())
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.consumerId(235L)
				.read(false)
				.author(authorModel)
				.build();
		notificationSimpleRepository.saveAndFlush(notificationSimpleModel);

		NotificationStampedModel notificationStampedModel = NotificationStampedModel.builder()
				.id(1L)
				.data(notificationSimpleRepository.findFirstByOrderByTimestampDesc().get())
				.read(false)
				.timestamp(NOW())
				.build();

		notificationStampedRepository.save(notificationStampedModel);
	}
	public void addData_One(){
		AuthorModel authorModel = AuthorModel.builder()
				.id(233L)
				.firstName("Artem")
				.lastName("Lebedev")
				.photo("photo url")
				.build();
		authorRepository.saveAndFlush(authorModel);

		NotificationSimpleModel notificationSimpleModel = NotificationSimpleModel.builder()
				.producerId(233L)
				.content("")
				.notificationType(ENotificationType.POST.name())
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.consumerId(235L)
				.read(false)
				.author(authorModel)
				.build();
		notificationSimpleRepository.saveAndFlush(notificationSimpleModel);

		NotificationStampedModel notificationStampedModel =	NotificationStampedModel.builder()
				.id(2L)
				.data(notificationSimpleRepository.findFirstByOrderByTimestampDesc().get())
				.read(false)
				.timestamp(NOW())
				.build();

		notificationStampedRepository.save(notificationStampedModel);
		NotificationCommonModel notificationCommonModel = NotificationCommonModel.builder()
				.id(2L)
				.producerId(233L)
				.content("")
				.eventId(UUID.fromString("3fa85f64-6543-4562-b3fc-2c963f66afa7"))
				.service(EServiceName.POSTS.name())
				.timestamp(NOW())
				.notificationType(ENotificationType.POST.name())
				.consumerId(235L)
				.read(false)
				.build();
		notificationCommonRepository.save(notificationCommonModel);
	}
	public void addData_Two(){
		AuthorModel authorModel = AuthorModel.builder()
				.id(233L)
				.firstName("Artem")
				.lastName("Lebedev")
				.photo("photo url")
				.build();
		authorRepository.saveAndFlush(authorModel);

		NotificationSimpleModel notificationSimpleModel = NotificationSimpleModel.builder()
				.producerId(233L)
				.content("")
				.notificationType(ENotificationType.POST.name())
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.consumerId(235L)
				.read(false)
				.author(authorModel)
				.build();
		notificationSimpleRepository.saveAndFlush(notificationSimpleModel);

		NotificationStampedModel notificationStampedModel =	NotificationStampedModel.builder()
				.id(2L)
				.data(notificationSimpleRepository.findFirstByOrderByTimestampDesc().get())
				.read(false)
				.timestamp(NOW())
				.build();

		notificationStampedRepository.save(notificationStampedModel);
		NotificationCommonModel notificationCommonModel = NotificationCommonModel.builder()
				.id(2L)
				.producerId(233L)
				.content("")
				.eventId(UUID.fromString("3fa85f64-6543-4562-b3fc-2c963f66afa7"))
				.service(EServiceName.POSTS.name())
				.timestamp(NOW())
				.notificationType(ENotificationType.POST.name())
				.consumerId(235L)
				.read(false)
				.build();
		notificationCommonRepository.save(notificationCommonModel);
	}
	public void addData_Three(){
		AuthorModel authorModel = AuthorModel.builder()
				.id(3L)
				.firstName("Artem")
				.lastName("Lebedev")
				.photo("photo url")
				.build();
		authorRepository.saveAndFlush(authorModel);

		NotificationSimpleModel notificationSimpleModel = NotificationSimpleModel.builder()
				.producerId(233L)
				.content("")
				.notificationType(ENotificationType.POST.name())
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.consumerId(235L)
				.read(false)
				.author(authorModel)
				.build();
		notificationSimpleRepository.saveAndFlush(notificationSimpleModel);

		NotificationStampedModel notificationStampedModel =	NotificationStampedModel.builder()
				.id(3L)
				.data(notificationSimpleRepository.findFirstByOrderByTimestampDesc().get())
				.read(false)
				.timestamp(NOW())
				.build();

		notificationStampedRepository.save(notificationStampedModel);
		NotificationCommonModel notificationCommonModel = NotificationCommonModel.builder()
				.id(3L)
				.producerId(233L)
				.content("")
				.eventId(UUID.fromString("3fa85f64-6543-4562-b3fc-2c963f66afa7"))
				.service(EServiceName.POSTS.name())
				.timestamp(NOW())
				.notificationType(ENotificationType.POST.name())
				.consumerId(235L)
				.read(false)
				.build();
		notificationCommonRepository.save(notificationCommonModel);
	}
}
