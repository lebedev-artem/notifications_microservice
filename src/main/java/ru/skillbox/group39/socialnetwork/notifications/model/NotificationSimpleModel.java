package ru.skillbox.group39.socialnetwork.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:30 <p>
 * description - dto события доп. <p>
 * id - DB id <p>
 * producerId - Id пользователя, создавшего событие <p>
 * content - описание события <p>
 * notificationType - тип уведомления <p>
 * sentTime - время события
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notifications_simple")
public class NotificationSimpleModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "producer_id")
	private Long producerId;

	@Column(name = "content")
	private String content;

	@Column(name = "notification_type")
	@Nullable
	private String notificationType;

	@Column(name = "timestamp")
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	@OneToOne(fetch = FetchType.EAGER, optional = false,cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(unique = true, name = "author")
	private AuthorModel author;
}
