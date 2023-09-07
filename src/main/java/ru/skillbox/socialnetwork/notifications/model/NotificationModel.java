package ru.skillbox.socialnetwork.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class NotificationModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;

	@Column(name = "author_id")
	private Integer authorId;

	@Column(name = "content")
	private String content;

	@Column(name = "notification_type")
	private String notificationType;

	@Column(name = "sent_time")
	private Timestamp sentTime;
}
