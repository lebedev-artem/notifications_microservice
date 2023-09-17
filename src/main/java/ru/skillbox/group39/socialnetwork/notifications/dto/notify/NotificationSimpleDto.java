package ru.skillbox.group39.socialnetwork.notifications.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.group39.socialnetwork.notifications.client.dto.Author;

import java.sql.Timestamp;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:30 <p>
 * description - dto события доп. <p>
 * producerId - Id пользователя, создавшего событие <p>
 * content - описание события <p>
 * notificationType - тип уведомления <p>
 * sentTime - время события
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSimpleDto {

	private Author author;
	private Long id;
	private ENotificationType notificationType;
	private Timestamp timestamp;
	private String content;
}
