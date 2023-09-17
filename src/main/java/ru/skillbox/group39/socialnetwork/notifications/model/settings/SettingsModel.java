package ru.skillbox.group39.socialnetwork.notifications.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@Column(name = "enable_like")
	private Boolean enableLike = false;
	@Column(name = "enable_post")
	private Boolean enablePost = true;
	@Column(name = "enable_comment")
	private Boolean enablePostComment = true;
	@Column(name = "enable_comment_comment")
	private Boolean enableCommentComment = false;
	@Column(name = "enable_message")
	private Boolean enableMessage = true;
	@Column(name = "enable_friend_request")
	private Boolean enableFriendRequest = false;
	@Column(name = "enable_friend_birthday")
	private Boolean enableFriendBirthday = false;
	@Column(name = "enable_send_email_message")
	private Boolean enableSendEmailMessage = false;
	@Column(name = "user_id")
	private Long userId;

	public SettingsModel(Long userId) {
		this.userId = userId;
	}
}
