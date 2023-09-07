package ru.skillbox.socialnetwork.notifications.dto.kafka;

import lombok.Getter;

/**
 * @author Artem Lebedev | 05/09/2023 - 20:42
 */

@Getter
public enum CommonNotifyTypeEnum {
	POST(1L),
	COMMENT(2L),
	ADD_FRIEND(3L),
	ADD_USER(4L),
	DEL_USER(5L),
	ADD_LIKE(6L),
	AVA_CHANGE(7L);

	private final Long id;

	CommonNotifyTypeEnum(Long id){
		this.id = id;
	}
}
