package ru.skillbox.socialnetwork.notifications.dto.kafka;

import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Artem Lebedev | 05/09/2023 - 11:58 <p>
 * @Description <p>
 * <b>Long\UUID (from ServiceNameEnum.java) initiatorId</b> : Инициатор события <p>
 * <b>String text</b>: Описание события <p>
 * <b>UUID objId</b>: Id сущности самого ивента, т.е. в post сервисе туда записывается id самого поста, или комментария, или лайка <p>
 * <b>Enum service (ServiceNameEnum.java)</b>: Сервис инициатор <p>
 * <b>Timestamp timestamp</b>: Дата\Время <p>
 * <b>String (CommonNotifyTypeEnum.java) type</b>: Тип события (пост, коммент, доб. в друзья, ...) <p>
 * <b>Long\UUID to</b>: Получатель <p>
 */

/*
Петя поставил лайк Васе под постом.
initiatorId - Id Пети в базе users;
text - 'Петя поставил вам Like';
objId - UUID самого event, генериться сервисом POSTS;
service - сервис инициатор уведомления, POSTS;
timestamp - Время создания уведомления;
type - тип Like;
to - Id Васи в базе users;
 */

@Data
public class CommonNotifyDto {
	private Long id;
	private String fromUserId;
	private String text;
	private UUID eventId;
	private ServiceNameEnum service;
	private Timestamp timestamp;
	private CommonNotifyTypeEnum type;
	private String toUserId;

	public CommonNotifyDto(String fromUserId,
	                       String text,
	                       UUID eventId,
	                       CommonNotifyTypeEnum type,
	                       String toUserId) {
		this.fromUserId = fromUserId;
		this.text = text;
		this.eventId = eventId;
		this.service = ServiceNameEnum.POSTS;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.type = type;
		this.toUserId = toUserId;
	}

	@Override
	public String toString() {
		return "\n    CommonNotifyDto" +
				"\n    Sender:            '" + fromUserId + '\'' +
				"\n    Text of notify:    '" + text + '\'' +
				"\n    Id of event:       '" + eventId + '\'' +
				"\n    Service-initiator: '" + service + '\'' +
				"\n    Timestamp:         '" + timestamp + '\'' +
				"\n    Type of event:     '" + type + '\'' +
				"\n    Acceptor:          '" + toUserId + "'\n";
	}
}
