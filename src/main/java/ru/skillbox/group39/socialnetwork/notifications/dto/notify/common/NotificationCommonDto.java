package ru.skillbox.group39.socialnetwork.notifications.dto.notify.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.group39.socialnetwork.notifications.dto.notify.ENotificationType;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Artem Lebedev | 05/09/2023 - 11:58 <p>
 * @Description <p>
 * <b>Long producerId</b> : Инициатор события <p>
 * <b>String content</b>: Описание события <p>
 * <b>UUID eventId</b>: Id сущности самого ивента, т.е. в post сервисе туда записывается id самого поста, или комментария, или лайка <p>
 * <b>EServiceName service (EServiceName.java)</b>: Сервис инициатор <p>
 * <b>Timestamp timestamp</b>: Дата\Время <p>
 * <b>ENotificationType (ENotificationType.java) notificationType</b>: Тип события (пост, коммент, доб. в друзья, ...) <p>
 * <b>Long to</b>: Получатель <p>
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationCommonDto {
	private Long id;
	private Long producerId;
	private String content;
	private UUID eventId;
	private EServiceName service;
	private Timestamp timestamp;
	private ENotificationType notificationType;
	private Long consumerId;

	public NotificationCommonDto(Long producerId,
	                             String content,
	                             UUID eventId,
	                             ENotificationType type,
	                             Long consumerId) {
		this.producerId = producerId;
		this.content = content;
		this.eventId = eventId;
		this.service = EServiceName.POSTS;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.notificationType = type;
		this.consumerId = consumerId;
	}

	@Override
	public String toString() {
		return "\n    NotificationCommonDto" +
				"\n    Producer:          '" + producerId + '\'' +
				"\n    Text of notify:    '" + content + '\'' +
				"\n    Id of event:       '" + eventId + '\'' +
				"\n    Service-initiator: '" + service + '\'' +
				"\n    Timestamp:         '" + timestamp + '\'' +
				"\n    Type of event:     '" + notificationType + '\'' +
				"\n    Consumer:          '" + consumerId + "'\n";
	}
}
