package ru.skillbox.socialnetwork.notifications.dto.notify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto события доп.")
public class NotificationDto {

	private String id;

	//Id пользователя, создавшего событие
	private Integer authorId;

	//Описание события
	private String content;

	//Тип события
	private String notificationType;

	//Время события
	private LocalDateTime sentTime;
}
