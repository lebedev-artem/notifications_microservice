package ru.skillbox.socialnetwork.notifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.socialnetwork.notifications.dto.Count;
import ru.skillbox.socialnetwork.notifications.dto.event.EventNotificationDto;
import ru.skillbox.socialnetwork.notifications.dto.notify.NotificationCountDto;
import ru.skillbox.socialnetwork.notifications.dto.notify.NotificationSettingDto;
import ru.skillbox.socialnetwork.notifications.dto.notify.NotificationUpdateDto;
import ru.skillbox.socialnetwork.notifications.dto.notify.PageNotificationsDto;
import ru.skillbox.socialnetwork.notifications.repositories.CommonNotifyRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@Tag(name = "NotificationModel service", description = "Сервис сообщений")
@Slf4j
@RequiredArgsConstructor
public class NotificationControllerImpl implements NotificationController {

	private final ObjectMapper objectMapper;
	private final HttpServletRequest request;
	private final CommonNotifyRepository commonNotifyRepository;

    public ResponseEntity<Void> addEvent(@Parameter(in = ParameterIn.DEFAULT, required = true)
                                         @Valid @RequestBody EventNotificationDto body) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

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

	public ResponseEntity<NotificationCountDto> getCount() {
		log.info(" * GET /count");
//		String accept = request.getHeader("Accept");
//		if (accept != null && accept.contains("application/json")) {
//			try {
//				return new ResponseEntity<NotificationCountDto>(objectMapper.readValue("{\n  \"timeStamp\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"data\" : {\n    \"count\" : 0\n  }\n}", NotificationCountDto.class), HttpStatus.NOT_IMPLEMENTED);
//			} catch (IOException e) {
//				log.error("Couldn't serialize response for content type application/json", e);
//				return new ResponseEntity<NotificationCountDto>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
		Count count = new Count(commonNotifyRepository.count());
		NotificationCountDto notificationCountDto = new NotificationCountDto(LocalDateTime.now(), count);
		return new ResponseEntity<>(notificationCountDto, HttpStatus.OK);
	}

	public ResponseEntity<PageNotificationsDto> getNotifications(@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = true, schema = @Schema()) @Valid @RequestParam(value = "page", required = true) Pageable page) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<PageNotificationsDto>(objectMapper.readValue("{\n  \"number\" : 1,\n  \"size\" : 5,\n  \"last\" : true,\n  \"numberOfElements\" : 5,\n  \"totalPages\" : 0,\n  \"pageable\" : {\n    \"paged\" : true,\n    \"pageNumber\" : 9,\n    \"offset\" : 2,\n    \"pageSize\" : 7,\n    \"unpaged\" : true\n  },\n  \"sort\" : {\n    \"unsorted\" : true,\n    \"sorted\" : true,\n    \"empty\" : true\n  },\n  \"content\" : [ {\n    \"timeStamp\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"data\" : {\n      \"sentTime\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n      \"notificationType\" : \"notificationType\",\n      \"authorId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n      \"content\" : \"content\"\n    }\n  }, {\n    \"timeStamp\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"data\" : {\n      \"sentTime\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n      \"notificationType\" : \"notificationType\",\n      \"authorId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n      \"content\" : \"content\"\n    }\n  } ],\n  \"first\" : true,\n  \"totalElements\" : 6,\n  \"empty\" : true\n}", PageNotificationsDto.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<PageNotificationsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<PageNotificationsDto>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<NotificationSettingDto> getSetting() {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<NotificationSettingDto>(objectMapper.readValue("{\n  \"enableMessage\" : true,\n  \"enableFriendBirthday\" : true,\n  \"enablePostComment\" : true,\n  \"enablePost\" : true,\n  \"enableFriendRequest\" : true,\n  \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",\n  \"enableCommentComment\" : true,\n  \"enableLike\" : true,\n  \"enableSendEmailMessage\" : true\n}", NotificationSettingDto.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<NotificationSettingDto>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<NotificationSettingDto>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Void> setIsRead() {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Void> updateSetting(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody NotificationUpdateDto body) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
}

