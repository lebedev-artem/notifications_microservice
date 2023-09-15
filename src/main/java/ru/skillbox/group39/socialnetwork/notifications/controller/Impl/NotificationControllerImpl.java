package ru.skillbox.group39.socialnetwork.notifications.controller.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.group39.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.NotificationSettingDto;
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.NotificationUpdateDto;
import ru.skillbox.group39.socialnetwork.notifications.exception.ErrorResponse;
import ru.skillbox.group39.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.group39.socialnetwork.notifications.controller.NotificationController;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Tag(name = "NotificationSimpleModel service", description = "Сервис сообщений")
@Slf4j
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

	private final NotificationService notificationService;

	@Override
	public Object getCount() {
		log.info(" * GET /count");
		return notificationService.getCount();
	}

	@Override
	public Object getPageNotifications(Integer page, Integer size, String sort) {
		log.info(" * GET \"/\"");
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		log.info(" * Pageable: {}", pageable);
		return notificationService.getPageStampedNotifications(pageable);
	}

	@Override
	public Object addEvent(@Parameter(in = ParameterIn.DEFAULT, required = true)
	                       @Valid @RequestBody EventNotificationDto request) {
		log.info(" * POST \"/addEvent\"");
		return notificationService.addEvent(request);
	}

	@Override
	public Object setAllRead(@NotNull HttpServletRequest request) {
		log.info(" * PUT \"/read\"");
		if (!(request.getParameterMap().isEmpty()) | (request.getHeader("Content-Type") != null)) {
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, "Wrong args/method/headers/etc"), HttpStatus.BAD_REQUEST);
		}
		return notificationService.setAllRead();
	}

	@Override
	public Object getSetting() {
		return new ResponseEntity<NotificationSettingDto>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Override
	public ResponseEntity<Boolean> createSetting(
			@Parameter(
					in = ParameterIn.PATH,
					description = "",
					required = true,
					schema = @Schema())
			@PathVariable("id") String id) {
		return new ResponseEntity<Boolean>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Override
	public ResponseEntity<Void> updateSetting(
			@Parameter(
					in = ParameterIn.DEFAULT,
					description = "",
					required = true,
					schema = @Schema())
			@Valid @RequestBody NotificationUpdateDto body) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
}

