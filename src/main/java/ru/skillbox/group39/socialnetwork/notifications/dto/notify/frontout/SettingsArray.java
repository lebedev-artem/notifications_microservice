package ru.skillbox.group39.socialnetwork.notifications.dto.notify.frontout;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Artem Lebedev | 11/10/2023 - 20:16
 */
@Data
@Builder
public class SettingsArray {
	List<SettingItem> notifications;
}
