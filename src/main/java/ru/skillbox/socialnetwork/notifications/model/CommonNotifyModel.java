package ru.skillbox.socialnetwork.notifications.model;

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
 *     See <b>CommonNotifyDto.java</b>
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commonnotifications")
public class CommonNotifyModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "from_user_id")
	private Long fromUserId;
	@Column(name = "text")
	private String text;
	@Column(name = "event_id")
	private UUID eventId;
	@Column(name = "service")
	private String service;
	@Column(name = "timestamp")
	private Timestamp timestamp;
	@Column(name = "type")
	private String type;
	@Column(name = "to_user_id")
	private Long toUserId;

	@Override
	public String toString() {
		return "\n    CommonNotifyModel" +
				"\n    id:                '" + id + '\'' +
				"\n    Sender:            '" + fromUserId + '\'' +
				"\n    Text of notify:    '" + text + '\'' +
				"\n    Id of event:       '" + eventId + '\'' +
				"\n    Service-initiator: '" + service + '\'' +
				"\n    Timestamp:         '" + timestamp + '\'' +
				"\n    Type of event:     '" + type + '\'' +
				"\n    Acceptor:          '" + toUserId + "'\n";
	}
}
