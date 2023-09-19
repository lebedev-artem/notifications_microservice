package ru.skillbox.group39.socialnetwork.notifications.dto.setting;

import lombok.Getter;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:50 <p>
 * Смотри описание к классу SettingsDto.class
 *
 */

@Getter
public enum ESettingsType {
	LIKE("enableLike"),
	POST("enablePost"),
	POST_COMMENT("enablePostComment"),
	COMMENT_COMMENT("enableCommentComment"),
	MESSAGE("enableMessage"),
	FRIEND_REQUEST("enableFriendRequest"),
	FRIEND_BIRTHDAY("enableFriendBirthday"),
	SEND_EMAIL_MESSAGE("enableSendEmailMessage");

	private final String type;

	ESettingsType(String type) {
		this.type = type;
	}
}
