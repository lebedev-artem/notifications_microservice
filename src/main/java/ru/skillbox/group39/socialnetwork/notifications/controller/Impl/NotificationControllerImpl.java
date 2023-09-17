package ru.skillbox.group39.socialnetwork.notifications.controller.Impl;

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
import ru.skillbox.group39.socialnetwork.notifications.dto.setting.SettingChangeDto;
import ru.skillbox.group39.socialnetwork.notifications.exception.ErrorResponse;
import ru.skillbox.group39.socialnetwork.notifications.service.NotificationService;
import ru.skillbox.group39.socialnetwork.notifications.controller.NotificationController;
import ru.skillbox.group39.socialnetwork.notifications.service.SettingsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Tag(name = "NotificationSimpleModel service", description = "Сервис сообщений")
@Slf4j
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

	private final NotificationService notificationService;
	private final SettingsService settingsService;

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
		return settingsService.getSettings();
	}

	@Override
	public Object createSetting(
			@Parameter(
					in = ParameterIn.PATH,
					description = "id пользователя в базе данных",
					required = true,
					schema = @Schema())
			@PathVariable("id") Long userId) {

		return settingsService.createSettings(userId);
	}

	@Override
	public Object changeSetting(
			@Parameter(
					in = ParameterIn.DEFAULT,
					description = "JSON с двумя полями: ESettingType, Boolean",
					required = true,
					schema = @Schema())
			@Valid @RequestBody SettingChangeDto settingChangeDto) {

		return settingsService.changeSetting(settingChangeDto);
	}
}

