package ru.skillbox.group39.socialnetwork.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Artem Lebedev | 12/09/2023 - 18:03
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications_stamped")
public class NotificationStampedModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "timestamp", nullable = false)
	private Timestamp timestamp;

	@OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(unique = true, name = "data")
	private NotificationSimpleModel data;

	public NotificationStampedModel(@NotNull NotificationSimpleModel data) {
//		this.id = data.getId();
		this.timestamp = data.getTimestamp();
		this.data = data;
	}
}
