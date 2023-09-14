package ru.skillbox.group39.socialnetwork.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Artem Lebedev | 06/09/2023 - 23:42 <p><p>
 *
 * @description <p>
 *     See <b>NotificationCommonDto.java</b>
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications_common")
public class NotificationCommonModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "producer_id")
	private Long producerId;
	@Column(name = "content")
	private String content;
	@Column(name = "event_id")
	private UUID eventId;
	@Column(name = "service")
	private String service;
	@Column(name = "timestamp")
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	@Column(name = "notification_type")
	private String notificationType;
	@Column(name = "consumer_id")
	private Long consumerId;

	@Override
	public String toString() {
		return "\n    NotificationCommonModel" +
				"\n    id:                '" + id + '\'' +
				"\n    Producer           '" + producerId + '\'' +
				"\n    Text of notify:    '" + content + '\'' +
				"\n    Id of event:       '" + eventId + '\'' +
				"\n    Service-initiator: '" + service + '\'' +
				"\n    Timestamp:         '" + timestamp + '\'' +
				"\n    Type of event:     '" + notificationType + '\'' +
				"\n    Consumer:          '" + consumerId + "'\n";
	}
}
