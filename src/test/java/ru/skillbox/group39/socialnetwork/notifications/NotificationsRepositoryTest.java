package ru.skillbox.group39.socialnetwork.notifications;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.ENotificationType;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.common.EServiceName;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.AuthorModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationCommonModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationSimpleModel;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationStampedModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.*;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.UUID;
import static ru.skillbox.group39.socialnetwork.notifications.utils.TimestampUtils.NOW;

/**
 * @author Artem Lebedev | 04/10/2023 - 10:46
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yaml"})
@Testcontainers
public class NotificationsRepositoryTest {
	private UUID notificationSimpleModelId;
	@Autowired
	NotificationStampedRepository notificationStampedRepository;
	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	NotificationSimpleRepository notificationSimpleRepository;
	@Autowired
	NotificationCommonRepository notificationCommonRepository;

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");
	@Container
	static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

	@BeforeAll
	static void beforeAll() {
		kafkaContainer.start();
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		kafkaContainer.stop();
		postgres.stop();
	}

	@DynamicPropertySource
	static void configureProperties(@NotNull DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@DynamicPropertySource
	static void kafkaProperties(@NotNull DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
	}

	@BeforeEach
	@Transactional
	public void setUp() {
		AuthorModel authorModel = AuthorModel.builder()
				.id(1L)
				.firstName("Artem")
				.lastName("Lebedev")
				.photo("photo url")
				.build();
		authorRepository.saveAndFlush(authorModel);

		NotificationSimpleModel notificationSimpleModel = NotificationSimpleModel.builder()
				.producerId(1L)
				.content("")
				.notificationType(ENotificationType.POST.name())
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.consumerId(2L)
				.read(false)
				.author(authorModel)
				.build();
		notificationSimpleRepository.saveAndFlush(notificationSimpleModel);

		NotificationStampedModel notificationStampedModel =	NotificationStampedModel.builder()
				.id(1L)
				.data(notificationSimpleRepository.findFirstByOrderByTimestampDesc().get())
				.read(false)
				.timestamp(NOW())
				.build();

		notificationStampedRepository.save(notificationStampedModel);

		NotificationCommonModel notificationCommonModel = NotificationCommonModel.builder()
				.id(1L)
				.producerId(1L)
				.content("")
				.eventId(UUID.fromString("3fa85f64-6543-4562-b3fc-2c963f66afa6"))
				.service(EServiceName.POSTS.name())
				.timestamp(NOW())
				.notificationType(ENotificationType.POST.name())
				.consumerId(2L)
				.read(false)
				.build();
		notificationCommonRepository.save(notificationCommonModel);
	}

	private void addData(){
		AuthorModel authorModel = AuthorModel.builder()
				.id(2L)
				.firstName("Artem")
				.lastName("Lebedev")
				.photo("photo url")
				.build();
		authorRepository.saveAndFlush(authorModel);

		NotificationSimpleModel notificationSimpleModel = NotificationSimpleModel.builder()
				.producerId(2L)
				.content("")
				.notificationType(ENotificationType.POST.name())
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.consumerId(3L)
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
				.producerId(2L)
				.content("")
				.eventId(UUID.fromString("3fa85f64-6543-4562-b3fc-2c963f66afa7"))
				.service(EServiceName.POSTS.name())
				.timestamp(NOW())
				.notificationType(ENotificationType.POST.name())
				.consumerId(3L)
				.read(false)
				.build();
		notificationCommonRepository.save(notificationCommonModel);
	}

	@Test
	@Transactional
	@Order(1)
	void getStampedWhenItemIsInBase_ReturnRealCount() throws Exception {
		Long cnt = (long) notificationStampedRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(1L, cnt);
	}

	@Test
	@Transactional
	@Order(2)
	void getSimpleWhenItemIsInBase_ReturnRealCount() throws Exception {
		Long cnt = (long) notificationSimpleRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(1L, cnt);
	}

	@Test
	@Transactional
	@Order(3)
	void getAuthorWhenItemIsInBase_ReturnRealCount() throws Exception {
		Long cnt = (long) authorRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(1L, cnt);
	}

	@Test
	@Transactional
	@Order(4)
	void getCommonWhenItemIsInBase_ReturnRealCount() throws Exception {
		Long cnt = (long) notificationCommonRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(1L, cnt);
	}


	@Test
	@Transactional
	@Order(5)
	void getStamped_afterAddOneStampedNotification_ReturnRealCount() throws Exception {
		addData();
		Long cnt = (long) notificationStampedRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(2L, cnt);
	}

	@Test
	@Transactional
	@Order(6)
	void getSimple_afterAddOneStampedNotification_ReturnRealCount() throws Exception {
		addData();
		Long cnt = (long) notificationSimpleRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(2L, cnt);
	}

	@Test
	@Transactional
	@Order(7)
	void getAuthor_afterAddOneStampedNotification_ReturnRealCount() throws Exception {
		addData();
		Long cnt = (long) authorRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(2L, cnt);
	}

	@Test
	@Transactional
	@Order(8)
	void getCommon_afterAddOneStampedNotification_ReturnRealCount() throws Exception {
		addData();
		Long cnt = (long) notificationCommonRepository.findAll().size();
		Assertions.assertNotNull(cnt);
		Assertions.assertEquals(2L, cnt);
	}

}
