package ru.skillbox.group39.socialnetwork.notifications.model.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:30 <p>
 * description - dto события доп. <p>
 * id - DB id <p>
 * producerId - Id пользователя, создавшего событие <p>
 * content - описание события <p>
 * notificationType - тип уведомления <p>
 * sentTime - время события
 * consumerId - получатель сообщения
 * read - статус прочтения
 * Author - автор уведомления. Dto на основе producerId
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notifications_simple")
@SQLDelete(sql = "UPDATE notifications_simple SET read = true WHERE id=?")
@Where(clause = "read=false")
public class NotificationSimpleModel {

	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
	private UUID id;

	@Column(name = "producer_id", nullable = false)
	private Long producerId;

	@Column(name = "content")
	private String content;

	@Column(name = "notification_type", nullable = false)
	@Nullable
	private String notificationType;

	@Column(name = "timestamp", nullable = false)
	private Timestamp timestamp;

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "read", nullable = false)
	private boolean read = Boolean.FALSE;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "author")
	private AuthorModel author;
}
