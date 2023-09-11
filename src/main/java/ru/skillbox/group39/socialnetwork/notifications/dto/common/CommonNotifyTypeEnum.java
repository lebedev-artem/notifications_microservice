package ru.skillbox.group39.socialnetwork.notifications.dto.common;

import lombok.Getter;

/**
 * @author Artem Lebedev | 05/09/2023 - 20:42
 */

@Getter
public enum CommonNotifyTypeEnum {
	POST(1L),
	POST_COMMENT(2L),
	COMMENT_COMMENT(3L),
	FRIEND_REQUEST(4L),
	FRIEND_BIRTHDAY(5L),
	MESSAGE(6L),
	LIKE(7L);

	private final Long id;

	CommonNotifyTypeEnum(Long id){
		this.id = id;
	}
}
