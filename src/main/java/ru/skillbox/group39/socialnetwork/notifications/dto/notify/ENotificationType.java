package ru.skillbox.group39.socialnetwork.notifications.dto.notify;

import lombok.Getter;

/**
 * @author Artem Lebedev | 05/09/2023 - 20:42 <p>
 *
POST(0L)<p>
POST_COMMENT(1L)<p>
COMMENT_COMMENT(2L)<p>
FRIEND_REQUEST(3L)<p>
FRIEND_BIRTHDAY(4L)<p>
MESSAGE(5L)<p>
DO_LIKE(6L)<p>
 */

@Getter
public enum ENotificationType {
	POST("POST"),
	POST_COMMENT("POST_COMMENT"),
	COMMENT_COMMENT("COMMENT_COMMENT"),
	FRIEND_REQUEST("FRIEND_REQUEST"),
	FRIEND_BIRTHDAY("FRIEND_BIRTHDAY"),
	MESSAGE("MESSAGES"),
	DO_LIKE("DO_LIKE"),
	DEL_USER("DEL_USER"),
	HANDY("HANDY");

	private final String asString;

	ENotificationType(String asString){
		this.asString = asString;
	}
}
