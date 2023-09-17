package ru.skillbox.group39.socialnetwork.notifications.dto.setting;

import lombok.Getter;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:50 <p>
 * Смотри описание к классу SettingsDto.class
 *
 */

@Getter
public enum ESettingsType {
	ENABLE_LIKE("enableLike"),
	ENABLE_POST("enablePost"),
	ENABLE_POST_COMMENT("enablePostComment"),
	ENABLE_COMMENT_COMMENT("enableCommentComment"),
	ENABLE_MESSAGE("enableMessage"),
	ENABLE_FRIEND_REQUEST("enableFriendRequest"),
	ENABLE_FRIEND_BIRTHDAY("enableFriendBirthday"),
	ENABLE_SEND_EMAIL_MESSAGE("enableSendEmailMessage");

	private final String type;

	ESettingsType(String type) {
		this.type = type;
	}
}
