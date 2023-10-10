package ru.skillbox.group39.socialnetwork.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.group39.socialnetwork.notifications.controller.Impl.NotificationControllerImpl;
import ru.skillbox.group39.socialnetwork.notifications.controller.NotificationController;
import ru.skillbox.group39.socialnetwork.notifications.dto.count.Count;
import ru.skillbox.group39.socialnetwork.notifications.dto.count.NotificationsCountDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.ENotificationType;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationSimpleDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.ESettingsType;
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.SettingChangeDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.SettingsDto;
import ru.skillbox.group39.socialnetwork.notifications.model.notification.NotificationSimpleModel;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationStampedRepository;
import ru.skillbox.group39.socialnetwork.notifications.security.jwt.JwtUtils;
import ru.skillbox.group39.socialnetwork.notifications.security.model.Person;
import ru.skillbox.group39.socialnetwork.notifications.security.service.UserDetailsServiceImpl;
import ru.skillbox.group39.socialnetwork.notifications.service.Impl.NotificationServiceImpl;
import ru.skillbox.group39.socialnetwork.notifications.service.Impl.SettingsServiceImpl;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Artem Lebedev | 03/10/2023 - 22:46
 */
@Slf4j
@AutoConfigureMockMvc(printOnlyOnFailure = false, addFilters = false)
@WebMvcTest(controllers = {NotificationControllerImpl.class, NotificationController.class})
@ExtendWith(MockitoExtension.class)
public class NotificationsApplicationControllerIT {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private JwtUtils jwtUtils;
	@MockBean
	private UserDetailsServiceImpl userDetailsService;
	@MockBean
	private SettingsServiceImpl settingsService;
	@MockBean
	private NotificationServiceImpl notificationService;
	@MockBean
	private NotificationStampedRepository notificationStampedRepository;
	private ObjectMapper objectMapper;
	private ModelMapper modelMapper;

	@BeforeEach
	void init() {
		objectMapper = new ObjectMapper();
		modelMapper = new ModelMapper();
	}

	@BeforeEach
	void setSecurityContext() {
		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername("artlebedev2006@gmail.com");

			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			log.error("Cannot set user authentication: {}", e.getMessage());
		}
	}

	@Test
	void getCount_Successful() throws Exception {
		Person person = userDetailsService.loadUserByUsername("artlebedev2006@gmail.com");
		String jwttoken = jwtUtils.generateToken(person, Duration.ofDays(1));
		var request = get("/api/v1/notifications/count")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding("utf-8")
				.header("Authorization", "Bearer " + jwttoken);
		NotificationsCountDto ncd = new NotificationsCountDto(LocalDateTime.now(), new Count(10L));
		when(notificationService.getCount()).thenReturn(ncd);
		mockMvc.
				perform(request)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count.count", is(10)));
	}

	@Test
	void getPageNotifications_Successful() throws Exception {
		Person person = userDetailsService.loadUserByUsername("artlebedev2006@gmail.com");
		String jwttoken = jwtUtils.generateToken(person, Duration.ofDays(1));
		var request = get("/api/v1/notifications/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding("utf-8")
				.header("Authorization", "Bearer " + jwttoken);
		Pageable pageable = PageRequest.of(0, 1, Sort.by("timestamp"));
		when(notificationService.getPageStampedNotifications(pageable)).thenReturn(any());
		mockMvc.
				perform(request)
				.andDo(print())
				.andExpect(status().isOk());
	}


	@Test
	void addEvent_Successful() throws Exception {
		EventNotificationDto end = EventNotificationDto.builder()
				.consumerId(233L)
				.producerId(232L)
				.content("Test content")
				.notificationType(ENotificationType.POST)
				.build();

		var request = post("/api/v1/notifications/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(end));

		NotificationSimpleModel nsm = NotificationSimpleModel.builder()
				.author(notificationService.getAuthorModelFromId(232L))
				.read(false)
				.consumerId(233L)
				.timestamp(new Timestamp(System.currentTimeMillis()))
				.notificationType("POST")
				.content("Test content")
				.producerId(232L)
				.build();
		ResponseEntity<?> response = new ResponseEntity<>(objectMapper.convertValue(nsm, NotificationSimpleDto.class), HttpStatus.OK);
		when(notificationService.addEvent(end)).thenReturn(response);
		mockMvc.
				perform(request)
				.andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	void putAllRead_Successful() throws Exception {

		var request = put("/api/v1/notifications/read");
		ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.OK);
		when(notificationService.setAllRead()).thenReturn(response);
		mockMvc.
				perform(request)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void getSettings_Successful() throws Exception {

		var request = get("/api/v1/notifications/settings");
		SettingsDto sdto = SettingsDto.builder()
				.DO_LIKE(true)
				.COMMENT_COMMENT(true)
				.FRIEND_BIRTHDAY(true)
				.FRIEND_REQUEST(true)
				.MESSAGE(true)
				.SEND_EMAIL_MESSAGE(false)
				.POST(true)
				.POST_COMMENT(false)
				.build();
		ResponseEntity<?> response = new ResponseEntity<>(sdto, HttpStatus.OK);
		when(settingsService.getSettings()).thenReturn(response);
		mockMvc.
				perform(request)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void createSettings_Successful() throws Exception {

		var request = post("/api/v1/notifications/settings")
				.param("userId", String.valueOf(233L));
		SettingsDto sdto = SettingsDto.builder()
				.DO_LIKE(true)
				.COMMENT_COMMENT(true)
				.FRIEND_BIRTHDAY(true)
				.FRIEND_REQUEST(true)
				.MESSAGE(true)
				.SEND_EMAIL_MESSAGE(false)
				.POST(true)
				.POST_COMMENT(false)
				.build();
		ResponseEntity<?> response = new ResponseEntity<>(sdto, HttpStatus.OK);
		when(settingsService.createSettings(anyLong())).thenReturn(response);
		mockMvc.
				perform(request)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void changeSettings_Successful() throws Exception {

		SettingChangeDto scdto = SettingChangeDto.builder()
				.notificationType(ESettingsType.POST)
				.enable(false)
				.build();
		var request = put("/api/v1/notifications/settings")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(scdto));

		ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.OK);
		when(settingsService.changeSetting(scdto)).thenReturn(response);
		mockMvc.
				perform(request)
				.andDo(print())
				.andExpect(status().isOk());
	}
}
