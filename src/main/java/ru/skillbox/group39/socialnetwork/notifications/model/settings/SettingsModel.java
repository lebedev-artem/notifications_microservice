package ru.skillbox.group39.socialnetwork.notifications.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	@JsonIgnore
	public Long id;
	@Column(name = "DO_LIKE")
	@JsonProperty("DO_LIKE")
	public Boolean DO_LIKE;
	@Column(name = "POST")
	@JsonProperty("POST")
	public Boolean POST;
	@Column(name = "POST_COMMENT")
	@JsonProperty("POST_COMMENT")
	public Boolean POST_COMMENT;
	@Column(name = "COMMENT_COMMENT")
	@JsonProperty("COMMENT_COMMENT")
	public Boolean COMMENT_COMMENT;
	@Column(name = "MESSAGE")
	@JsonProperty("MESSAGE")
	public Boolean MESSAGE;
	@Column(name = "FRIEND_REQUEST")
	@JsonProperty("FRIEND_REQUEST")
	public Boolean FRIEND_REQUEST;
	@Column(name = "FRIEND_BIRTHDAY")
	@JsonProperty("FRIEND_BIRTHDAY")
	public Boolean FRIEND_BIRTHDAY;
	@Column(name = "SEND_EMAIL_MESSAGE")
	@JsonProperty("SEND_EMAIL_MESSAGE")
	public Boolean SEND_EMAIL_MESSAGE;
	@Column(name = "user_id")
	@JsonIgnore
	public Long userId;

	public SettingsModel(Long userId) {
		this.userId = userId;
	}
}
