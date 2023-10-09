package ru.skillbox.group39.socialnetwork.notifications;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.AccountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.ENotificationType;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationStampedRepository;
import ru.skillbox.group39.socialnetwork.notifications.security.service.UserDetailsServiceImpl;
import ru.skillbox.group39.socialnetwork.notifications.service.Impl.NotificationServiceImpl;


import javax.transaction.Transactional;
import java.sql.Timestamp;


/**
 * @author Artem Lebedev | 05/10/2023 - 09:21
 */

@Slf4j
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class NotificationsServiceIT {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	@Autowired
	NotificationServiceImpl notificationService;
	@Autowired
	NotificationStampedRepository notificationStampedRepository;
	@Autowired
	DBInitializer dbInitializer;

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
		dbInitializer.setUp();
	}

	@BeforeEach
	void setSecurityContext() {
		AccountDto accountDto = new AccountDto(
				233L,
				"Artem",
				"Lebedev",
				"artlebedev2006@gmail.com",
				"$2a$10$ctRxKRl77dh0WB47uip8yuNnCmdSqB/X0SvYLUkHscG00N9pXyrpa",
				"USER");
		UserDetails userDetails;
		try {
			userDetails = userDetailsService.loadUserByUsername("artlebedev2006@gmail.com");
		}
		catch (Exception e) {
			log.error("Cannot get user authentication: {}", e.getMessage());
			log.error("Authentication will set by hand");
			userDetails = userDetailsService.getHandyPerson(accountDto);
		}
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	void getPageNotifications_Successful() throws Exception {
		Long consumerId = UserDetailsServiceImpl.getPrincipalId();
		Pageable pageable = PageRequest.of(0, 1, Sort.by("timestamp"));
//		Page<NotificationStampedModel> page = notificationStampedRepository.findAllByData_ConsumerId(consumerId, pageable);
//		notificationStampedRepository.deleteAll(page);
		ResponseEntity<?> response = (ResponseEntity<?>) notificationService.getPageStampedNotifications(pageable);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}


	@Test
	@Transactional
	void addEvent_Successful() throws Exception {
		EventNotificationDto end = EventNotificationDto.builder()
				.consumerId(233L)
				.producerId(235L)
				.content("addEvent_Successful")
				.notificationType(ENotificationType.POST)
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.build();
		ResponseEntity<?> response = (ResponseEntity<?>) notificationService.addEvent(end);
		Assertions.assertNotNull(response);
		Assertions.assertEquals(1, notificationStampedRepository.countByData_Content(end.getContent()));
	}

	@Test
	void setAllRead_Successful() throws Exception {
		dbInitializer.addData_One();
		dbInitializer.addData_Two();
		dbInitializer.addData_Three();
		ResponseEntity<?> response = (ResponseEntity<?>) notificationService.setAllRead();
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}


}
