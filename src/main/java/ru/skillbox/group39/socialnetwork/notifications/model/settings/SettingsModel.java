package ru.skillbox.group39.socialnetwork.notifications.model.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Artem Lebedev | 16/09/2023 - 22:01
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notifications_settings")
public class SettingsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "LIKE")
	private Boolean LIKE = false;
	@Column(name = "POST")
	private Boolean POST = true;
	@Column(name = "COMMENT")
	private Boolean POST_COMMENT = true;
	@Column(name = "COMMENT_COMMENT")
	private Boolean COMMENT_COMMENT = false;
	@Column(name = "MESSAGE")
	private Boolean MESSAGE = true;
	@Column(name = "FRIEND_REQUEST")
	private Boolean FRIEND_REQUEST = false;
	@Column(name = "FRIEND_BIRTHDAY")
	private Boolean FRIEND_BIRTHDAY = false;
	@Column(name = "SEND_EMAIL_MESSAGE")
	private Boolean ENABLE_EMAIL_MESSAGE = false;
	@Column(name = "user_id")
	private Long userId;

	public SettingsModel(Long userId) {
		this.userId = userId;
	}
}
