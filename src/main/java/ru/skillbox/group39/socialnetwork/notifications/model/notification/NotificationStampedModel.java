package ru.skillbox.group39.socialnetwork.notifications.model.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Artem Lebedev | 12/09/2023 - 18:03
 */

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE notifications_stamped SET read=true WHERE id=?")
@Where(clause = "read=false")
@Table(name = "notifications_stamped")
public class NotificationStampedModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "timestamp", nullable = false)
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(unique = true, name = "data")
	private NotificationSimpleModel data;

	@Column(name = "read")
	private boolean read = Boolean.FALSE;

	public NotificationStampedModel(@NotNull NotificationSimpleModel data) {
		this.timestamp = data.getTimestamp();
		this.data = data;
	}
}
