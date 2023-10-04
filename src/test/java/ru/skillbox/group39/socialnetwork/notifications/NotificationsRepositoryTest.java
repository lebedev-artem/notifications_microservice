package ru.skillbox.group39.socialnetwork.notifications;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
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
import ru.skillbox.group39.socialnetwork.notifications.repositories.*;
import ru.skillbox.group39.socialnetwork.notifications.service.Impl.NotificationServiceImpl;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Artem Lebedev | 04/10/2023 - 10:46
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yaml"})
@Testcontainers
public class NotificationsRepositoryTest {

	@Autowired
	NotificationCommonRepository notificationCommonRepository;

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@Container
	static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
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

//	TODO
//	@Order
	@Test
	@Transactional
	@Order(1)
	public void processCommonModelTest_Successful() {
		AuthorModel am = AuthorModel.builder()
				.firstName("Artem")
				.lastName("Lebedev")
				.photo("url")
				.build();
		NotificationSimpleModel nsm = NotificationSimpleModel.builder()
				.author(am)
				.read(false)
				.consumerId(233L)
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.notificationType("POST")
				.content("Test content")
				.producerId(232L)
				.build();
		NotificationCommonModel ncm = new NotificationCommonModel(
				null,
				232L,
				"Test content",
				UUID.fromString("3fa85f64-6543-4562-b3fc-2c963f66afa6"),
				EServiceName.POSTS.name(),
				new Timestamp(System.currentTimeMillis()),
				ENotificationType.LIKE.name(),
				233L,
				false);

		notificationCommonRepository.save(ncm);
		Long ncmid = ncm.getId();
		assertNotNull(ncmid);
	}

}
