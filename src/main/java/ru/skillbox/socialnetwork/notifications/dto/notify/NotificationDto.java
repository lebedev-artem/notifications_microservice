package ru.skillbox.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:30 <p>
 * description - dto события доп. <p>
 * authorId - Id пользователя, создавшего событие <p>
 * content - описание события <p>
 * notificationType - тип уведомления <p>
 * sentTime - время события
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

	private String id;
	private Integer authorId;
	private String content;
	private NotificationType notificationType;
	private Timestamp sentTime;
}
