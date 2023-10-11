package ru.skillbox.group39.socialnetwork.notifications.dto.notify.frontout;

import lombok.Builder;
import lombok.Data;

/**
 * @author Artem Lebedev | 11/10/2023 - 20:12
 */
@Data
@Builder
public class SettingItem {
	private String notificationType;
	private Boolean enable;

}
