package ru.skillbox.group39.socialnetwork.notifications.controller.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationSettingDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.NotificationUpdateDto;
import ru.skillbox.group39.socialnetwork.notifications.repositories.NotificationCommonRepository;
import ru.skillbox.group39.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.group39.socialnetwork.notifications.controller.NotificationController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Tag(name = "NotificationSimpleModel service", description = "Сервис сообщений")
@Slf4j
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

	private final ObjectMapper objectMapper;
	private final HttpServletRequest request;
	private final NotificationCommonRepository notificationCommonRepository;
	private final NotificationService notificationService;

	@Override
	public Object getCount() {
		log.info(" * GET /count");

		Pageable pageable = PageRequest.of(0, 1, Sort.by("timestamp"));

		return notificationService.getCount(pageable);
	}

	@Override
	public Object getPageNotifications(Integer page, Integer size, String sort) {
		log.info(" * GET \"/\"");

		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		log.info(" * Pageable: {}", pageable);

		// TODO
		// + must return stamped page
		// 2. check fields id
		// + create AuthorModel model
		return notificationService.getPageStampedNotifications(pageable);
	}

	@Override
	public Object addEvent(@Parameter(in = ParameterIn.DEFAULT, required = true)
	                                     @Valid @RequestBody EventNotificationDto request) {

		return notificationService.addEvent(request);
	}

	@Override
	public Object getSetting() {

		return new ResponseEntity<NotificationSettingDto>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Override
	public ResponseEntity<Boolean> createSetting(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("id") String id) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Boolean>(objectMapper.readValue("true", Boolean.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Boolean>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Override
	public ResponseEntity<Void> setIsRead() {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
	@Override
	public ResponseEntity<Void> updateSetting(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody NotificationUpdateDto body) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
}

