package ru.skillbox.group39.socialnetwork.notifications.dto.notify;

import lombok.Getter;

/**
 * @author Artem Lebedev | 24/08/2023 - 19:50 <p>
 * Смотри описание к классу NotificationSettingDto.class
 *
 */

@Getter
public enum NotificationSettingType {
	ENABLE_LIKE("ENABLE_LIKE"),
	ENABLE_POST("ENABLE_POST"),
	ENABLE_POST_COMMENT("ENABLE_POST_COMMENT"),
	ENABLE_COMMENT_COMMENT("ENABLE_COMMENT_COMMENT"),
	ENABLE_MESSAGE("ENABLE_MESSAGE"),
	ENABLE_FRIEND_REQUEST("ENABLE_FRIEND_REQUEST"),
	ENABLE_FRIEND_BIRTHDAY("ENABLE_FRIEND_BIRTHDAY"),
	ENABLE_SEND_EMAIL_MESSAGE("ENABLE_SEND_EMAIL_MESSAGE");

	private final String type;

	NotificationSettingType(String type) {
		this.type = type;
	}
}
